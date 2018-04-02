package net.swiftzer.eric.twopanedemo.delivery.list

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.delivery_list_fragment.*
import net.swiftzer.eric.twopanedemo.*
import net.swiftzer.eric.twopanedemo.db.DeliveryDao
import net.swiftzer.eric.twopanedemo.network.DeliveryApi
import net.swiftzer.eric.twopanedemo.network.entities.Delivery
import org.jetbrains.anko.support.v4.dimen
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Eric on 3/25/2018.
 */
class DeliveryListFragment : Fragment() {
    companion object {
        fun newInstance(onItemClickedCallback: (delivery: Delivery) -> Unit) = DeliveryListFragment().apply {
            this.onItemClickedCallback = onItemClickedCallback
        }
    }

    @Inject
    internal lateinit var deliveryApi: DeliveryApi
    @Inject
    internal lateinit var deliveryDao: DeliveryDao
    private lateinit var viewModel: DeliveryListViewModel
    private lateinit var layoutManager: LinearLayoutManager

    var onItemClickedCallback: (delivery: Delivery) -> Unit = {}
    private lateinit var adapter: DeliveryListAdapter
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val component = TwoPaneApplication.instance.appComponent
                .deliveryBuilder()
                .build()
        component.inject(this)

        viewModel = viewModelOf(DeliveryListViewModel.Factory(deliveryApi, deliveryDao))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.delivery_list_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutManager = LinearLayoutManager(this@DeliveryListFragment.context)
        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                Timber.d("onLoadMore() called with: page = [$page], totalItemsCount = [$totalItemsCount], view = [$view]")
            }
        }
        adapter = DeliveryListAdapter(onItemClickedCallback, this::onRetryCallback)
        with(recyclerView) {
            adapter = this@DeliveryListFragment.adapter
            layoutManager = this@DeliveryListFragment.layoutManager
            addItemDecoration(
                    LinearSpacingItemDecoration(
                            topEdgePadding = dimen(R.dimen.delivery_list_item_vertical_outer_margin),
                            bottomEdgePadding = dimen(R.dimen.delivery_list_item_vertical_outer_margin),
                            leftEdgePadding = dimen(R.dimen.delivery_list_item_horizontal_margin),
                            rightEdgePadding = dimen(R.dimen.delivery_list_item_horizontal_margin),
                            verticalInnerSpacing = dimen(R.dimen.delivery_list_item_vertical_inner_margin)
                    )
            )
            addOnScrollListener(scrollListener)
        }

        viewModel.listLiveData.observe(this, Observer { it?.let { adapter.submitList(it) } })
        viewModel.loadDelivery(0)
    }

    private fun updateList(newList: List<Delivery>) {
//        adapter.update(newList)
    }

    private fun onRetryCallback() {
        Timber.d("onRetryCallback() called")
    }
}
