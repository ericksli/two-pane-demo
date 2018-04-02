package net.swiftzer.eric.twopanedemo

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView

abstract class EndlessScrollRecyclerViewAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    protected var itemList: List<T> = emptyList()
    protected var endOfList: Boolean = false
    protected var endItemShowLoading: Boolean = true
    abstract val loadingItemId: Long

    abstract fun getDiffCallback(oldList: List<T>, newList: List<T>): DiffCallback<T>

    abstract fun getLoadingItem(): T

    override fun getItemCount(): Int = itemList.size

    /**
     * Update the list
     */
    fun submitList(newList: List<T>, loadingState: LoadingState, endOfList: Boolean) {
        val newListWithLoading = if (endOfList) newList else newList + getLoadingItem()
        val diffResult = DiffUtil.calculateDiff(getDiffCallback(itemList, newListWithLoading))
        diffResult.dispatchUpdatesTo(this)
        itemList = newListWithLoading
        this.endOfList = endOfList
        this.endItemShowLoading = !endOfList && loadingState != LoadingState.FAIL
        notifyItemChanged(itemCount - 1)
    }
}
