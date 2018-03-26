package net.swiftzer.eric.twopanedemo.delivery.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.delivery_list_item.*
import net.swiftzer.eric.twopanedemo.GlideApp
import net.swiftzer.eric.twopanedemo.R
import net.swiftzer.eric.twopanedemo.network.entities.Delivery

/**
 * Created by Eric on 3/25/2018.
 */
class DeliveryListAdapter(
        private var deliveries: List<Delivery>,
        private val onItemClickedCallback: (delivery: Delivery) -> Unit
) : RecyclerView.Adapter<DeliveryViewHolder>() {
    override fun getItemCount(): Int = deliveries.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(DeliveryViewHolder.LAYOUT_ID, parent, false)
        return DeliveryViewHolder(view, onItemClickedCallback)
    }

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        holder.bind(deliveries[position])
    }

    fun update(newList: List<Delivery>) {
        deliveries = newList
        notifyDataSetChanged()
    }
}

class DeliveryViewHolder(
        override val containerView: View?,
        private val onItemClickedCallback: (delivery: Delivery) -> Unit
) : RecyclerView.ViewHolder(containerView), LayoutContainer, View.OnClickListener {
    companion object {
        const val LAYOUT_ID = R.layout.delivery_list_item
    }

    fun bind(delivery: Delivery) {
        GlideApp.with(itemView)
                .load(delivery.imageUrl)
                .placeholder(R.drawable.image_placeholder)
                .centerCrop()
                .into(thumbnail)
        title.text = delivery.description
        itemView.tag = delivery
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val delivery = v.tag as Delivery
        onItemClickedCallback(delivery)
    }
}
