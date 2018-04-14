package net.swiftzer.eric.twopanedemo

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.View

/**
 * Extension function to simplify the creation of [ViewModel] with [ViewModelProvider.Factory].
 */
inline fun <reified T : ViewModel> FragmentActivity.viewModelOf(factory: ViewModelProvider.Factory): T =
        ViewModelProviders.of(this, factory).get(T::class.java)

/**
 * Extension function to simplify the creation of [ViewModel] with [ViewModelProvider.Factory].
 */
inline fun <reified T : ViewModel> Fragment.viewModelOf(factory: ViewModelProvider.Factory): T =
        ViewModelProviders.of(this, factory).get(T::class.java)

/**
 * Extension function to simplify the creation of [ViewModel].
 */
inline fun <reified T : ViewModel> FragmentActivity.viewModelOf(): T =
        ViewModelProviders.of(this).get(T::class.java)

/**
 * Extension function to simplify the creation of [ViewModel].
 */
inline fun <reified T : ViewModel> Fragment.viewModelOf(): T =
        ViewModelProviders.of(this).get(T::class.java)

/**
 * Simple extension function to type fewer characters.
 * @see View.VISIBLE
 */
@Suppress("NOTHING_TO_INLINE")
inline fun View.visible() {
    visibility = View.VISIBLE
}

/**
 * Simple extension function to type fewer characters.
 * @see View.GONE
 */
@Suppress("NOTHING_TO_INLINE")
inline fun View.gone() {
    visibility = View.GONE
}

/**
 * Simple extension function to type fewer characters.
 * @see View.INVISIBLE
 */
@Suppress("NOTHING_TO_INLINE")
inline fun View.invisible() {
    visibility = View.INVISIBLE
}
