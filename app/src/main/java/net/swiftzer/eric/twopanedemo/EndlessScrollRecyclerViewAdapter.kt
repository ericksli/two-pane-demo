package net.swiftzer.eric.twopanedemo

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import timber.log.Timber

abstract class EndlessScrollRecyclerViewAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    protected var itemList: List<T> = emptyList()

    abstract val loadingItemId: Long

    abstract fun getDiffCallback(oldList: List<T>, newList: List<T>): DiffCallback<T>

    fun isLoadingItem(position: Int): Boolean = position != 0 && position == itemCount - 1

    override fun getItemCount(): Int {
        Timber.d("getItemCount() called ${itemList.size}")
        return if (itemList.isNotEmpty()) itemList.size + 1 else 0
    }

    /**
     * Update the list
     */
    fun submitList(newList: List<T>) {
        val diffResult = DiffUtil.calculateDiff(getDiffCallback(itemList, newList))
        diffResult.dispatchUpdatesTo(this)
        itemList = newList
        notifyItemChanged(itemCount - 1)
    }
}