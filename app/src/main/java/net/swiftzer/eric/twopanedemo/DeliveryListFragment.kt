package net.swiftzer.eric.twopanedemo

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.delivery_list_fragment.*
import net.swiftzer.eric.twopanedemo.network.DeliveryApi
import org.jetbrains.anko.support.v4.dimen
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
    private lateinit var viewModel: DeliveryListViewModel

    var onItemClickedCallback: (delivery: Delivery) -> Unit = {}
    private lateinit var adapter: DeliveryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val component = TwoPaneApplication.instance.appComponent
                .deliveryBuilder()
                .deliveryListFragment(this)
                .build()
        component.inject(this)

        viewModel = viewModelOf(DeliveryListViewModel.Factory(deliveryApi))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.delivery_list_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.listLiveData.observe(this, Observer { newList ->
            newList?.let { updateList(it) }
        })

        adapter = DeliveryListAdapter(emptyList(), onItemClickedCallback)
        with(recyclerView) {
            adapter = this@DeliveryListFragment.adapter
            layoutManager = LinearLayoutManager(this@DeliveryListFragment.context)
            addItemDecoration(
                    LinearSpacingItemDecoration(
                            topEdgePadding = dimen(R.dimen.delivery_list_item_vertical_outer_margin),
                            bottomEdgePadding = dimen(R.dimen.delivery_list_item_vertical_outer_margin),
                            leftEdgePadding = dimen(R.dimen.delivery_list_item_horizontal_margin),
                            rightEdgePadding = dimen(R.dimen.delivery_list_item_horizontal_margin),
                            verticalInnerSpacing = dimen(R.dimen.delivery_list_item_vertical_inner_margin)
                    )
            )
        }

        viewModel.loadDelivery()
    }

    private fun updateList(newList: List<Delivery>) {
        adapter.update(newList)
    }
}
