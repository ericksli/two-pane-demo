package net.swiftzer.eric.twopanedemo

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView

/**
 * Base [RecyclerView] adapter class for generic infinite scroll list.
 */
abstract class EndlessScrollRecyclerViewAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    protected var itemList: List<T> = emptyList()
    protected var endOfList: Boolean = false
    protected var endItemShowLoading: Boolean = true
    abstract val loadingItemId: Long

    /**
     * Returns the [DiffCallback] for the list.
     */
    abstract fun getDiffCallback(oldList: List<T>, newList: List<T>): DiffCallback<T>

    /**
     * Returns an item to represent loading/error list item at the end of list.
     */
    abstract fun getLoadingItem(): T

    override fun getItemCount(): Int = itemList.size

    /**
     * Update the list.
     * @param newList the new whole list to be rendered
     * @param loadingState list loading state
     * @param endOfList whether the list has reached to the end
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
