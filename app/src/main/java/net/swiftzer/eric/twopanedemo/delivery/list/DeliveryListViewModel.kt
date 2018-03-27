package net.swiftzer.eric.twopanedemo.delivery.list

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import net.swiftzer.eric.twopanedemo.DELIVERY_LIST_RESPONSE_PER_PAGE
import net.swiftzer.eric.twopanedemo.db.DeliveryDao
import net.swiftzer.eric.twopanedemo.db.entities.CachedDelivery
import net.swiftzer.eric.twopanedemo.network.DeliveryApi

/**
 * Created by eric on 26/3/2018.
 */
class DeliveryListViewModel(
        private val deliveryApi: DeliveryApi,
        deliveryDao: DeliveryDao
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    val listLiveData: LiveData<PagedList<CachedDelivery>>

    init {
        val pagedListConfig = PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPageSize(DELIVERY_LIST_RESPONSE_PER_PAGE)
                .build()
        listLiveData = LivePagedListBuilder(deliveryDao.getDeliveries(), pagedListConfig).build()
    }

//    fun loadDelivery(offset: Int = 0) {
//        compositeDisposable += deliveryApi.deliveries(offset)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//                    Timber.d("loadDelivery: success %s", it)
//                }, { e ->
//                    Timber.e(e, "loadDelivery: error")
//                })
//    }

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
