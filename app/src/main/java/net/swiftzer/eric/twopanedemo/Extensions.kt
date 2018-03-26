package net.swiftzer.eric.twopanedemo

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

inline fun <reified T : ViewModel> FragmentActivity.viewModelOf(factory: ViewModelProvider.Factory): T =
        ViewModelProviders.of(this, factory).get(T::class.java)

inline fun <reified T : ViewModel> Fragment.viewModelOf(factory: ViewModelProvider.Factory): T =
        ViewModelProviders.of(this, factory).get(T::class.java)

inline fun <reified T : ViewModel> FragmentActivity.viewModelOf(): T =
        ViewModelProviders.of(this).get(T::class.java)

inline fun <reified T : ViewModel> Fragment.viewModelOf(): T =
        ViewModelProviders.of(this).get(T::class.java)
