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
import net.swiftzer.eric.twopanedemo.db.DeliveryDao
import net.swiftzer.eric.twopanedemo.network.DeliveryApi
import timber.log.Timber

/**
 * Created by eric on 26/3/2018.
 */
class DeliveryListViewModel(
        deliveryApi: DeliveryApi,
        deliveryDao: DeliveryDao
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val repository: DeliveryListRepository = DeliveryListRepository(deliveryApi, deliveryDao)
    val stateLiveData: MutableLiveData<DeliveryListState> = MutableLiveData()

    init {
        stateLiveData.value = DeliveryListState()
    }

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

        stateLiveData.postValue(prevState.copy(offset = prevState.offset, loadingState = LoadingState.LOADING))
        compositeDisposable += repository.loadDeliveries(prevState.offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("loadDelivery: success, size = ${it.size}")
                    val oldState = stateLiveData.value ?: DeliveryListState()
                    val newList = oldState.itemList + it
                    val offset = if (prevState.offset == 0) {
                        DELIVERY_LIST_RESPONSE_PER_PAGE + 1
                    } else {
                        prevState.offset + DELIVERY_LIST_RESPONSE_PER_PAGE
                    }
                    stateLiveData.postValue(oldState.copy(
                            itemList = newList,
                            loadingState = LoadingState.SUCCESS,
                            offset = offset,
                            endOfList = it.size < DELIVERY_LIST_RESPONSE_PER_PAGE
                    ))
                }, { e ->
                    Timber.e(e, "loadDelivery: error")
                    val oldState = stateLiveData.value ?: DeliveryListState()
                    stateLiveData.postValue(oldState.copy(
                            loadingState = LoadingState.FAIL,
                            endOfList = false
                    ))
                })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    class Factory(private val deliveryApi: DeliveryApi, private val deliveryDao: DeliveryDao) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val viewModel = DeliveryListViewModel(deliveryApi, deliveryDao)
            return viewModel as T
        }
    }
}
