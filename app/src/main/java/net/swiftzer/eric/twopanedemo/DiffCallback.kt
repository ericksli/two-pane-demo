package net.swiftzer.eric.twopanedemo

import android.support.v7.util.DiffUtil

abstract class DiffCallback<T>(
        protected val oldList: List<T>,
        protected val newList: List<T>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size
}