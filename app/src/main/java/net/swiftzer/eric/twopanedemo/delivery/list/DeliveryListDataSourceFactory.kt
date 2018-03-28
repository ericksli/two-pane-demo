package net.swiftzer.eric.twopanedemo.delivery.list

import android.arch.paging.DataSource
import io.reactivex.disposables.CompositeDisposable
import net.swiftzer.eric.twopanedemo.db.entities.CachedDelivery
import net.swiftzer.eric.twopanedemo.network.DeliveryApi

class DeliveryListDataSourceFactory(
        private val deliveryApi: DeliveryApi,
        private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, CachedDelivery>() {
    override fun create(): DataSource<Int, CachedDelivery> =
            DeliveryListDataSource(deliveryApi, compositeDisposable)
}
