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

/**
 * Repository for retrieving delivery list items.
 *
 * It caches the response from backend server after loaded. Next time, it will not send request to
 * backend server and just load from DB.
 */
class DeliveryListRepository @Inject constructor(
        private val deliveryApi: DeliveryApi,
        private val deliveryDao: DeliveryDao
) {
    /**
     * Load deliveries items from cache DB or network.
     *
     * It tries to load from DB first. If miss, load from network and cache the response to DB.
     *
     * @param offset pagination offset
     */
    fun loadDeliveries(offset: Int): Single<List<CachedDelivery>> {
        Timber.d("loadDeliveries() called with: offset = [%d]", offset)
        return Maybe.concat(loadDeliveriesFromCache(offset), loadDeliveriesFromNetwork(offset))
                .firstElement()
                .toSingle()
    }

    /**
     * Load deliveries items from DB.
     * @param offset pagination offset
     */
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

    /**
     * Load deliveries items from network and then cache it to DB.
     * @param offset pagination offset
     */
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

    /**
     * Write deliveries to DB for caching.
     * @param list list of items to be written to DB
     */
    private fun writeDeliveriesToCache(list: List<CachedDelivery>) = Completable.fromAction {
        Timber.d("writeDeliveriesToCache() called size = [%d], first item = [%s]", list.size, list.firstOrNull())
        deliveryDao.insert(list)
    }

    /**
     * Clear all delivery list items stored in DB.
     */
    fun clearCache(): Completable = Completable.fromAction {
        deliveryDao.deleteAll()
    }
}
