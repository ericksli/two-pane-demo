package net.swiftzer.eric.twopanedemo.delivery.list

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import net.swiftzer.eric.twopanedemo.network.DeliveryApi
import net.swiftzer.eric.twopanedemo.network.entities.Delivery
import timber.log.Timber

/**
 * Created by eric on 26/3/2018.
 */
class DeliveryListViewModel(private val deliveryApi: DeliveryApi) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    val listLiveData = MutableLiveData<List<Delivery>>()

    fun loadDelivery(offset: Int = 0) {
        compositeDisposable += deliveryApi.deliveries(offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("loadDelivery: success %s", it)
                    listLiveData.postValue(it)
                }, { e ->
                    Timber.e(e, "loadDelivery: error")
                })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    class Factory(private val deliveryApi: DeliveryApi) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val viewModel = DeliveryListViewModel(deliveryApi)
            return viewModel as T
        }
    }
}
