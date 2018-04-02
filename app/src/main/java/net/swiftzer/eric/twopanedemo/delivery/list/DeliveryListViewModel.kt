package net.swiftzer.eric.twopanedemo.delivery.list

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import net.swiftzer.eric.twopanedemo.db.DeliveryDao
import net.swiftzer.eric.twopanedemo.db.entities.CachedDelivery
import net.swiftzer.eric.twopanedemo.delivery.DeliveryListRepository
import net.swiftzer.eric.twopanedemo.network.DeliveryApi
import timber.log.Timber

/**
 * Created by eric on 26/3/2018.
 */
class DeliveryListViewModel(
        private val deliveryApi: DeliveryApi,
        private val deliveryDao: DeliveryDao
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val repository: DeliveryListRepository
    var listLiveData: MutableLiveData<List<CachedDelivery>> = MutableLiveData()

    init {
        repository = DeliveryListRepository(deliveryApi, compositeDisposable)
//        val pagedListConfig = PagedList.Config.Builder()
//                .setEnablePlaceholders(true)
//                .setPageSize(DELIVERY_LIST_RESPONSE_PER_PAGE)
//                .build()
//        listLiveData = LivePagedListBuilder(deliveryDao.getDeliveries(), pagedListConfig).build()
    }

    fun loadDeliveries() {
        Timber.d("loadDeliveries() called")
//        listLiveData = repository.loadDeliveries(DELIVERY_LIST_RESPONSE_PER_PAGE)
    }

    fun loadDelivery(offset: Int = 0) {
        compositeDisposable += deliveryApi.deliveries(offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("loadDelivery: success %s", it)
                    val cachedDeliveries = it.mapIndexed { index, delivery -> delivery.toCachedDelivery(index + 1) }
                    val newList = listLiveData.value?.let { it + cachedDeliveries }
                            ?: kotlin.run { cachedDeliveries }
                    listLiveData.postValue(newList)
                }, { e ->
                    Timber.e(e, "loadDelivery: error")
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
