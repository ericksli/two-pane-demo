package net.swiftzer.eric.twopanedemo.delivery.list

import net.swiftzer.eric.twopanedemo.LoadingState
import net.swiftzer.eric.twopanedemo.db.entities.CachedDelivery

data class DeliveryListState(
        val itemList: List<CachedDelivery> = emptyList(),
        val offset: Int = 0,
        val loadingState: LoadingState = LoadingState.LOADING,
        val endOfList: Boolean = false
)


