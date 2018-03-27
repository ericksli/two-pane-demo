package net.swiftzer.eric.twopanedemo.delivery.list

import android.arch.paging.PagedList
import net.swiftzer.eric.twopanedemo.db.entities.CachedDelivery

/**
 * Created by eric on 27/3/2018.
 */
class DeliveryListBoundaryCallback : PagedList.BoundaryCallback<CachedDelivery>() {
    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
    }

    override fun onItemAtEndLoaded(itemAtEnd: CachedDelivery) {
        super.onItemAtEndLoaded(itemAtEnd)
    }

    override fun onItemAtFrontLoaded(itemAtFront: CachedDelivery) {
        super.onItemAtFrontLoaded(itemAtFront)
    }
}
