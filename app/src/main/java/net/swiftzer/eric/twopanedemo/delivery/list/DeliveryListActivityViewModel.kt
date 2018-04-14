package net.swiftzer.eric.twopanedemo.delivery.list

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * View model for [DeliveryListActivity].
 */
class DeliveryListActivityViewModel(private val repository: DeliveryListRepository) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val clearCacheLiveData = MutableLiveData<Boolean>()

    /**
     * Clear all cached data in DB.
     */
    fun clearLocalCache() {
        Timber.d("clearLocalCache() called")
        compositeDisposable += repository.clearCache()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    clearCacheLiveData.postValue(true)
                }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    /**
     * Factory for instantiating [DeliveryListActivityViewModel].
     */
    class Factory(private val repository: DeliveryListRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val viewModel = DeliveryListActivityViewModel(repository)
            return viewModel as T
        }
    }
}
