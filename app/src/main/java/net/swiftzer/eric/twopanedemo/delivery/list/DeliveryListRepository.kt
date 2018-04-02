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

class DeliveryListRepository(
        private val deliveryApi: DeliveryApi,
        private val deliveryDao: DeliveryDao
) {
    fun loadDeliveries(offset: Int): Single<List<CachedDelivery>> {
        Timber.d("loadDeliveries() called with: offset = [$offset]")

        val cacheMaybe = deliveryDao.getDeliveriesByOffset(DELIVERY_LIST_RESPONSE_PER_PAGE, offset - 1)
                .flatMapMaybe {
                    Timber.d("loadDeliveries dao called size = ${it.size}")
                    if (it.isEmpty()) {
                        Maybe.empty()
                    } else {
                        Maybe.just(it)
                    }
                }
        val networkMaybe = loadDeliveriesFromNetwork(offset)
                .flatMap {
                    Singles.zip(
                            Single.just(it),
                            writeToCache(it).toSingle { emptyList<CachedDelivery>() }
                    )
                }
                .flatMapMaybe { (response, _) ->
                    Timber.d("loadDeliveries net called size = ${response.size}")
                    if (response.isEmpty()) {
                        Maybe.empty()
                    } else {
                        Maybe.just(response)
                    }
                }

        return Maybe.concat(cacheMaybe, networkMaybe)
                .firstElement()
                .toSingle()
    }

    private fun loadDeliveriesFromNetwork(offset: Int) = deliveryApi.deliveries(offset).map {
        Timber.d("loadDeliveriesFromNetwork() called offset = $offset")
        val startIndex = if (offset == 0) {
            1
        } else {
            offset
        }
        it.mapIndexed { index, delivery -> delivery.toCachedDelivery(startIndex + index) }
    }

    private fun writeToCache(list: List<CachedDelivery>) = Completable.fromAction {
        Timber.d("writeToCache() called size = ${list.size}, first = ${list.firstOrNull()}")
        deliveryDao.insert(list)
    }
}
