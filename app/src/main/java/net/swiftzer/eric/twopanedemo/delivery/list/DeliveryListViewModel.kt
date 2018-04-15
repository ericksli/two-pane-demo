package net.swiftzer.eric.twopanedemo.delivery.list

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import net.swiftzer.eric.twopanedemo.DELIVERY_LIST_RESPONSE_PER_PAGE
import net.swiftzer.eric.twopanedemo.LoadingState
import net.swiftzer.eric.twopanedemo.db.entities.CachedDelivery
import timber.log.Timber

/**
 * View model for [DeliveryListFragment].
 */
class DeliveryListViewModel(private val repository: DeliveryListRepository) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    val stateLiveData: MutableLiveData<DeliveryListState> = MutableLiveData()

    init {
        stateLiveData.value = DeliveryListState()
    }

    /**
     * Trigger load delivery from [DeliveryListRepository].
     */
    fun loadDelivery() {
        val prevState = stateLiveData.value ?: DeliveryListState()

        if (prevState.loadingState == LoadingState.LOADING && prevState.offset != 0) {
            Timber.d("loadDelivery: loading offset %d now, skip", prevState.offset)
            return
        }
        if (prevState.endOfList) {
            Timber.d("loadDelivery: end of list already, skip")
            return
        }
        Timber.d("loadDelivery: load offset %d", prevState.offset)

        stateLiveData.postValue(prevState.copy(
                offset = prevState.offset,
                loadingState = LoadingState.LOADING
        ))
        compositeDisposable += repository.loadDeliveries(prevState.offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { onLoadDeliverySuccess(it, prevState.offset) },
                        this::onLoadDeliveryError
                )
    }

    /**
     * Handle delivery list successfully loaded.
     * @param deliveries list of cached delivery items
     * @param prevOffset previous offset
     */
    private fun onLoadDeliverySuccess(deliveries: List<CachedDelivery>, prevOffset: Int) {
        Timber.d("onLoadDeliverySuccess: size = %d", deliveries.size)
        val oldState = stateLiveData.value ?: DeliveryListState()
        val newList = oldState.itemList + deliveries
        val offset = if (prevOffset == 0) {
            DELIVERY_LIST_RESPONSE_PER_PAGE + 1
        } else {
            prevOffset + DELIVERY_LIST_RESPONSE_PER_PAGE
        }
        stateLiveData.postValue(oldState.copy(
                itemList = newList,
                loadingState = LoadingState.SUCCESS,
                offset = offset,
                endOfList = deliveries.size < DELIVERY_LIST_RESPONSE_PER_PAGE
        ))
    }

    /**
     * Handle delivery list error case.
     * @param e throwable for causing this error
     */
    private fun onLoadDeliveryError(e: Throwable) {
        Timber.e(e, "onLoadDeliveryError")
        val oldState = stateLiveData.value ?: DeliveryListState()
        stateLiveData.postValue(oldState.copy(
                loadingState = LoadingState.FAIL,
                endOfList = false
        ))
    }

    fun refreshDelivery() {
        stateLiveData.postValue(DeliveryListState())
        compositeDisposable += repository.clearCache()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    loadDelivery()
                }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    /**
     * Factory for [DeliveryListViewModel].
     */
    class Factory(private val repository: DeliveryListRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val viewModel = DeliveryListViewModel(repository)
            return viewModel as T
        }
    }
}
