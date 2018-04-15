package net.swiftzer.eric.twopanedemo.delivery.list

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.delivery_list_fragment.*
import net.swiftzer.eric.twopanedemo.*
import net.swiftzer.eric.twopanedemo.network.entities.Delivery
import org.jetbrains.anko.support.v4.dimen
import timber.log.Timber
import javax.inject.Inject

/**
 * Fragment shows the actual list of delivery items.
 */
class DeliveryListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    companion object {
        fun newInstance(onItemClickedCallback: (delivery: Delivery) -> Unit) = DeliveryListFragment().apply {
            this.onItemClickedCallback = onItemClickedCallback
        }
    }

    @Inject
    internal lateinit var deliveryListRepository: DeliveryListRepository
    private lateinit var viewModel: DeliveryListViewModel
    private lateinit var layoutManager: LinearLayoutManager

    var onItemClickedCallback: (delivery: Delivery) -> Unit = {}
    private lateinit var adapter: DeliveryListAdapter
    /** Scroll listener for infinite scroll */
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val component = TwoPaneApplication.instance.appComponent
                .deliveryBuilder()
                .build()
        component.inject(this)

        viewModel = viewModelOf(DeliveryListViewModel.Factory(deliveryListRepository))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.delivery_list_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        viewModel.stateLiveData.observe(this, Observer(this::onStateChanged))

        if (savedInstanceState == null) {
            viewModel.loadDelivery()
        }
    }

    /**
     * Set up views.
     */
    private fun initViews() {
        swipeRefreshLayout.setOnRefreshListener(this)
        layoutManager = LinearLayoutManager(this@DeliveryListFragment.context)
        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                Timber.d("onLoadMore() called with: page = [%d], totalItemsCount = [%d]", page, totalItemsCount)
                if (page > 1) {
                    viewModel.loadDelivery()
                }
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
        retryBtn.setOnClickListener { onRetryCallback() }
    }

    /**
     * Callback when state live data is changed.
     * @param newState new state object
     */
    private fun onStateChanged(newState: DeliveryListState?) {
        Timber.d("onStateChanged() called with: newState = [$newState]")
        newState?.let {
            if (it.offset == 0) {
                when (it.loadingState) {
                    LoadingState.LOADING -> {
                        recyclerView.gone()
                        progressBar.visible()
                        errorGroup.gone()
                    }
                    LoadingState.SUCCESS -> {
                        recyclerView.visible()
                        progressBar.gone()
                        errorGroup.gone()
                        swipeRefreshLayout.isRefreshing = false
                    }
                    LoadingState.FAIL -> {
                        recyclerView.gone()
                        progressBar.gone()
                        errorGroup.visible()
                        swipeRefreshLayout.isRefreshing = false
                    }
                }
            } else {
                recyclerView.visible()
                progressBar.gone()
                errorGroup.gone()
            }

            adapter.submitList(
                    newList = it.itemList,
                    loadingState = it.loadingState,
                    endOfList = it.endOfList
            )
        }
    }

    override fun onRefresh() {
        Timber.d("onRefresh() called")
        viewModel.refreshDelivery()
    }

    /**
     * Callback when retry button is clicked.
     */
    private fun onRetryCallback() {
        Timber.d("onRetryCallback() called")
        viewModel.loadDelivery()
    }
}
