package net.swiftzer.eric.twopanedemo.delivery.list

import android.arch.paging.PageKeyedDataSource
import net.swiftzer.eric.twopanedemo.network.entities.Delivery

/**
 * Created by Eric on 3/26/2018.
 */
class DeliveryListDataSource : PageKeyedDataSource<Int, Delivery>() {
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Delivery>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Delivery>) {
        // ignored, since we only ever append to our initial load
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Delivery>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
