package net.swiftzer.eric.twopanedemo

import android.support.v7.util.DiffUtil


/**
 * Base class for implementing [DiffUtil.Callback].
 */
abstract class DiffCallback<out T>(
        protected val oldList: List<T>,
        protected val newList: List<T>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size
}