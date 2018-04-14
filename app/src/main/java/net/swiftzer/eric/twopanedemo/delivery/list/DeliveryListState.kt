package net.swiftzer.eric.twopanedemo.delivery.list

import net.swiftzer.eric.twopanedemo.LoadingState
import net.swiftzer.eric.twopanedemo.db.entities.CachedDelivery

/**
 * State object for [DeliveryListFragment].
 */
data class DeliveryListState(
        /** List of loaded items */
        val itemList: List<CachedDelivery> = emptyList(),
        /** Pagination offset */
        val offset: Int = 0,
        /** State for loading list items */
        val loadingState: LoadingState = LoadingState.LOADING,
        /** Whether the list is reached the end */
        val endOfList: Boolean = false
)
