package net.swiftzer.eric.twopanedemo.delivery.list

import android.arch.paging.PageKeyedDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import net.swiftzer.eric.twopanedemo.db.entities.CachedDelivery
import net.swiftzer.eric.twopanedemo.network.DeliveryApi
import timber.log.Timber

/**
 * Created by Eric on 3/26/2018.
 */
class DeliveryListDataSource(
        private val deliveryApi: DeliveryApi,
        private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, CachedDelivery>() {
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, CachedDelivery>) {
        Timber.d("loadInitial() called with: params = [$params]")
        compositeDisposable += deliveryApi.deliveries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ res ->
                    Timber.d("loadInitial: success")
                    val cachedDeliveries = res.mapIndexed { index, delivery -> delivery.toCachedDelivery(index + 1) }
                    callback.onResult(cachedDeliveries, 0, res.size + 1)
                }, { e ->
                    Timber.e(e, "loadInitial: error")
                    callback.onResult(emptyList(), 0, null)
                })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, CachedDelivery>) {
        Timber.d("loadBefore() called with: params = [$params], callback = [$callback]")
        // ignored, since we only ever append to our initial load
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, CachedDelivery>) {
        Timber.d("loadAfter() called with: params = [$params]")
        compositeDisposable += deliveryApi.deliveries(params.key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ res ->
                    Timber.d("loadInitial: success")
                    val startId = params.key
                    val cachedDeliveries = res.mapIndexed { index, delivery -> delivery.toCachedDelivery(startId + index) }
                    callback.onResult(cachedDeliveries, startId + res.size)
                }, { e ->
                    Timber.e(e, "loadInitial: error")
                    callback.onResult(emptyList(), null)
                })
    }
}
