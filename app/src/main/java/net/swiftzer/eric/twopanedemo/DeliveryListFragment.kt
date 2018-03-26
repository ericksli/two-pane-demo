package net.swiftzer.eric.twopanedemo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.delivery_list_fragment.*
import org.jetbrains.anko.support.v4.dimen

/**
 * Created by Eric on 3/25/2018.
 */
class DeliveryListFragment : Fragment() {
    companion object {
        fun newInstance(onItemClickedCallback: (delivery: Delivery) -> Unit) = DeliveryListFragment().apply {
            this.onItemClickedCallback = onItemClickedCallback
        }
    }

    var onItemClickedCallback: (delivery: Delivery) -> Unit = {}
    private lateinit var adapter: DeliveryListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.delivery_list_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = listOf(
                Delivery("test1", "https://upload.cc/i2/RbQ4aq.jpg", DeliveryLocation(22.336083, 114.155275, "Un Chau Estate")),
                Delivery("test2", "https://upload.cc/i2/VGcaZo.jpg", DeliveryLocation(22.336083, 114.155275, "Un Chau Estate")),
                Delivery("test3", "https://upload.cc/i2/tJAoLD.jpg", DeliveryLocation(22.336083, 114.155275, "Un Chau Estate")),
                Delivery("test4", "https://upload.cc/i2/RbQ4aq.jpg", DeliveryLocation(22.336083, 114.155275, "Un Chau Estate")),
                Delivery("test5", "https://upload.cc/i2/VGcaZo.jpg", DeliveryLocation(22.336083, 114.155275, "Un Chau Estate")),
                Delivery("test6", "https://upload.cc/i2/tJAoLD.jpg", DeliveryLocation(22.336083, 114.155275, "Un Chau Estate")),
                Delivery("test7", "https://upload.cc/i2/RbQ4aq.jpg", DeliveryLocation(22.336083, 114.155275, "Un Chau Estate")),
                Delivery("test8", "https://upload.cc/i2/VGcaZo.jpg", DeliveryLocation(22.336083, 114.155275, "Un Chau Estate")),
                Delivery("test9", "https://upload.cc/i2/tJAoLD.jpg", DeliveryLocation(22.336083, 114.155275, "Un Chau Estate"))
        )
        adapter = DeliveryListAdapter(list, onItemClickedCallback)
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
    }
}
