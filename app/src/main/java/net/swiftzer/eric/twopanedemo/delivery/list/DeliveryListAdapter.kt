package net.swiftzer.eric.twopanedemo.delivery.list

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.delivery_list_item.*
import net.swiftzer.eric.twopanedemo.GlideApp
import net.swiftzer.eric.twopanedemo.R
import net.swiftzer.eric.twopanedemo.db.entities.CachedDelivery
import net.swiftzer.eric.twopanedemo.gone
import net.swiftzer.eric.twopanedemo.network.entities.Delivery
import net.swiftzer.eric.twopanedemo.network.entities.DeliveryLocation
import net.swiftzer.eric.twopanedemo.visible

/**
 * Created by Eric on 3/25/2018.
 */
class DeliveryListAdapter(
        private val onItemClickedCallback: (delivery: Delivery) -> Unit
) : PagedListAdapter<CachedDelivery, DeliveryViewHolder>(CachedDeliveryDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(DeliveryViewHolder.LAYOUT_ID, parent, false)
        return DeliveryViewHolder(view, onItemClickedCallback)
    }

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        val delivery = getItem(position)
        if (delivery != null) {
            holder.bind(delivery)
        } else {
            holder.clear()
        }
    }
}


class DeliveryViewHolder(
        override val containerView: View?,
        private val onItemClickedCallback: (delivery: Delivery) -> Unit
) : RecyclerView.ViewHolder(containerView), LayoutContainer, View.OnClickListener {
    companion object {
        const val LAYOUT_ID = R.layout.delivery_list_item
    }

    fun bind(delivery: CachedDelivery) {
        cardView.visible()
        progressBar.gone()
        GlideApp.with(itemView)
                .load(delivery.imageUrl)
                .placeholder(R.drawable.image_placeholder)
                .centerCrop()
                .into(thumbnail)
        title.text = delivery.description
        itemView.tag = delivery
        itemView.setOnClickListener(this)
    }

    fun clear() {
        cardView.gone()
        progressBar.visible()
        GlideApp.with(itemView).clear(thumbnail)
    }

    override fun onClick(v: View) {
        val cachedDelivery = v.tag as CachedDelivery
        val delivery = Delivery(
                description = cachedDelivery.description,
                imageUrl = cachedDelivery.imageUrl,
                location = DeliveryLocation(
                        lat = cachedDelivery.lat,
                        lng = cachedDelivery.lng,
                        address = cachedDelivery.address
                )
        )
        onItemClickedCallback(delivery)
    }
}


object CachedDeliveryDiffCallback : DiffUtil.ItemCallback<CachedDelivery>() {
    override fun areItemsTheSame(oldItem: CachedDelivery?, newItem: CachedDelivery?): Boolean = oldItem?.id == newItem?.id
    override fun areContentsTheSame(oldItem: CachedDelivery?, newItem: CachedDelivery?): Boolean = oldItem == newItem
}
