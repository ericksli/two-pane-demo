package net.swiftzer.eric.twopanedemo.delivery.list

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.rxkotlin.Singles
import net.swiftzer.eric.twopanedemo.DELIVERY_LIST_RESPONSE_PER_PAGE
import net.swiftzer.eric.twopanedemo.db.DeliveryDao
import net.swiftzer.eric.twopanedemo.db.entities.CachedDelivery
import net.swiftzer.eric.twopanedemo.network.DeliveryApi
import timber.log.Timber
import javax.inject.Inject

class DeliveryListRepository @Inject constructor(
        private val deliveryApi: DeliveryApi,
        private val deliveryDao: DeliveryDao
) {
    fun loadDeliveries(offset: Int): Single<List<CachedDelivery>> {
        Timber.d("loadDeliveries() called with: offset = [%d]", offset)
        return Maybe.concat(loadDeliveriesFromCache(offset), loadDeliveriesFromNetwork(offset))
                .firstElement()
                .toSingle()
    }

    private fun loadDeliveriesFromCache(offset: Int) =
            deliveryDao.getDeliveriesByOffset(DELIVERY_LIST_RESPONSE_PER_PAGE, offset - 1)
                    .flatMapMaybe {
                        Timber.d("loadDeliveriesFromCache called size = [%d]", it.size)
                        if (it.isEmpty()) {
                            Maybe.empty()
                        } else {
                            Maybe.just(it)
                        }
                    }

    private fun loadDeliveriesFromNetwork(offset: Int) =
            deliveryApi.deliveries(offset)
                    .map {
                        Timber.d("loadDeliveriesFromNetwork net called size = [%d]", it.size)
                        val startIndex = if (offset == 0) {
                            1
                        } else {
                            offset
                        }
                        it.mapIndexed { index, delivery -> delivery.toCachedDelivery(startIndex + index) }
                    }
                    .flatMap {
                        Singles.zip(
                                Single.just(it),
                                writeDeliveriesToCache(it).toSingle { emptyList<CachedDelivery>() }
                        )
                    }
                    .flatMapMaybe { (response, _) ->
                        if (response.isEmpty()) {
                            Maybe.empty()
                        } else {
                            Maybe.just(response)
                        }
                    }

    private fun writeDeliveriesToCache(list: List<CachedDelivery>) = Completable.fromAction {
        Timber.d("writeDeliveriesToCache() called size = [%d], first item = [%s]", list.size, list.firstOrNull())
        deliveryDao.insert(list)
    }

    fun clearCache() = Completable.fromAction {
        deliveryDao.deleteAll()
    }
}
