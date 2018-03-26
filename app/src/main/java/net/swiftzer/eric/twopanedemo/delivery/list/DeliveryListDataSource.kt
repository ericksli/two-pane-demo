package net.swiftzer.eric.twopanedemo.delivery.list

import android.arch.paging.PositionalDataSource
import net.swiftzer.eric.twopanedemo.network.entities.Delivery

/**
 * Created by Eric on 3/26/2018.
 */
class DeliveryListDataSource : PositionalDataSource<Delivery>() {
    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Delivery>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Delivery>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}