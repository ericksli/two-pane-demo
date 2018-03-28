package net.swiftzer.eric.twopanedemo.delivery

import android.arch.lifecycle.LiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.support.annotation.MainThread
import io.reactivex.disposables.CompositeDisposable
import net.swiftzer.eric.twopanedemo.db.entities.CachedDelivery
import net.swiftzer.eric.twopanedemo.delivery.list.DeliveryListDataSourceFactory
import net.swiftzer.eric.twopanedemo.network.DeliveryApi
import timber.log.Timber

class DeliveryListRepository(
        private val deliveryApi: DeliveryApi,
        private val compositeDisposable: CompositeDisposable
) {
    @MainThread
    fun loadDeliveries(pageSize: Int): LiveData<PagedList<CachedDelivery>> {
        Timber.d("loadDeliveries() called with: pageSize = [$pageSize]")
        val sourceFactory = DeliveryListDataSourceFactory(deliveryApi, compositeDisposable)
        val livePagedList = LivePagedListBuilder(sourceFactory, pageSize)
                .build()
        return livePagedList
    }
}
