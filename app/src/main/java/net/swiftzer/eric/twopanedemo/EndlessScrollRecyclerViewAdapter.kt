package net.swiftzer.eric.twopanedemo

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView

abstract class EndlessScrollRecyclerViewAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    protected var itemList: List<T> = emptyList()
    protected var endOfList: Boolean = false
    protected var endItemShowLoading: Boolean = true
    abstract val loadingItemId: Long

    abstract fun getDiffCallback(oldList: List<T>, newList: List<T>): DiffCallback<T>

    fun isLoadingItem(position: Int): Boolean = position != 0 && position == itemCount - 1 && !endOfList

    override fun getItemCount(): Int = when {
        itemList.isEmpty() -> 0
        endOfList -> itemList.size
        else -> // last item is the loading item
            itemList.size + 1
    }

    /**
     * Update the list
     */
    fun submitList(newList: List<T>, loadingState: LoadingState, endOfList: Boolean) {
        val diffResult = DiffUtil.calculateDiff(getDiffCallback(itemList, newList))
        diffResult.dispatchUpdatesTo(this)
        itemList = newList
        this.endOfList = endOfList
        this.endItemShowLoading = !endOfList && loadingState != LoadingState.FAIL
        notifyItemChanged(itemCount - 1)
    }
}
