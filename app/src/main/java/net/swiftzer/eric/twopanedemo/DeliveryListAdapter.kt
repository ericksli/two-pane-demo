package net.swiftzer.eric.twopanedemo

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.delivery_list_item.*

/**
 * Created by Eric on 3/25/2018.
 */
class DeliveryListAdapter(private val deliveries: List<Delivery>) : RecyclerView.Adapter<DeliveryViewHolder>() {
    override fun getItemCount(): Int = deliveries.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(DeliveryViewHolder.LAYOUT_ID, parent, false)
        return DeliveryViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        holder.bind(deliveries[position])
    }
}

class DeliveryViewHolder(override val containerView: View?) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    companion object {
        const val LAYOUT_ID = R.layout.delivery_list_item
    }

    fun bind(delivery: Delivery) {
        Glide.with(itemView)
                .load(delivery.imageUrl)
                .into(thumbnail)
        title.text = delivery.description
    }
}