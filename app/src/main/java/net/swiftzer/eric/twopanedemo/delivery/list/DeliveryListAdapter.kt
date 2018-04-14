package net.swiftzer.eric.twopanedemo.delivery.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.delivery_list_item.*
import kotlinx.android.synthetic.main.delivery_list_item_loading.*
import net.swiftzer.eric.twopanedemo.*
import net.swiftzer.eric.twopanedemo.db.entities.CachedDelivery
import net.swiftzer.eric.twopanedemo.network.entities.Delivery
import net.swiftzer.eric.twopanedemo.network.entities.DeliveryLocation

/**
 * Recycler view adapter for delivery list in [DeliveryListFragment].
 */
class DeliveryListAdapter(
        private val onItemClickedCallback: (delivery: Delivery) -> Unit,
        private val onRetryCallback: () -> Unit
) : EndlessScrollRecyclerViewAdapter<CachedDelivery, RecyclerView.ViewHolder>() {

    init {
        // Cached items have ID
        setHasStableIds(true)
    }

    override fun getItemViewType(position: Int): Int =
            if (itemList[position].id.toLong() == loadingItemId) {
                LoadingViewHolder.LAYOUT_ID
            } else {
                DeliveryViewHolder.LAYOUT_ID
            }

    override val loadingItemId: Long
        get() = -9999L

    override fun getLoadingItem(): CachedDelivery = CachedDelivery(id = loadingItemId.toInt())

    override fun getItemId(position: Int): Long = itemList[position].id.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        LoadingViewHolder.LAYOUT_ID -> {
            val view = LayoutInflater.from(parent.context).inflate(LoadingViewHolder.LAYOUT_ID, parent, false)
            LoadingViewHolder(view, onRetryCallback)
        }
        DeliveryViewHolder.LAYOUT_ID -> {
            val view = LayoutInflater.from(parent.context).inflate(DeliveryViewHolder.LAYOUT_ID, parent, false)
            DeliveryViewHolder(view, onItemClickedCallback)
        }
        else -> throw IllegalArgumentException("Unsupported type $viewType")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DeliveryViewHolder -> holder.bind(itemList[position])
            is LoadingViewHolder -> holder.bind(endItemShowLoading)
        }
    }

    override fun getDiffCallback(oldList: List<CachedDelivery>, newList: List<CachedDelivery>): DiffCallback<CachedDelivery> =
            CachedDeliveryDiffCallback(oldList, newList)
}


/**
 * View holder for delivery list item.
 * @param containerView item view
 * @param onItemClickedCallback callback when item is clicked
 */
class DeliveryViewHolder(
        override val containerView: View?,
        private val onItemClickedCallback: (delivery: Delivery) -> Unit
) : RecyclerView.ViewHolder(containerView), LayoutContainer, View.OnClickListener {
    companion object {
        const val LAYOUT_ID = R.layout.delivery_list_item
    }

    /**
     * Bind the view holder with [CachedDelivery].
     * @param delivery item to render
     */
    fun bind(delivery: CachedDelivery) {
        GlideApp.with(itemView)
                .load(delivery.imageUrl)
                .placeholder(R.drawable.image_placeholder)
                .centerCrop()
                .into(thumbnail)
        title.text = delivery.description
        itemView.tag = delivery
        itemView.setOnClickListener(this)
    }

    /**
     * Handle click event for the whole item.
     */
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


/**
 * View holder for loading item.
 * @param containerView item view
 * @param onRetryCallback callback when retry button is clicked
 */
class LoadingViewHolder(
        override val containerView: View?,
        private val onRetryCallback: () -> Unit
) : RecyclerView.ViewHolder(containerView), LayoutContainer, View.OnClickListener {
    companion object {
        const val LAYOUT_ID = R.layout.delivery_list_item_loading
    }

    /**
     * Bind the view holder with loading/error state.
     * @param isLoading whether the list is loading
     */
    fun bind(isLoading: Boolean) {
        if (isLoading) {
            progressBar.visible()
            errorGroup.gone()
        } else {
            progressBar.gone()
            errorGroup.visible()
        }
        retryBtn.setOnClickListener(this)
    }

    /**
     * Retry button click event handling.
     */
    override fun onClick(v: View) {
        onRetryCallback()
    }
}


/**
 * Recycler view diff callback for [CachedDelivery].
 * @param oldList old list
 * @param newList new list
 */
class CachedDeliveryDiffCallback(
        oldList: List<CachedDelivery>,
        newList: List<CachedDelivery>
) : DiffCallback<CachedDelivery>(oldList, newList) {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition].id == newList[newItemPosition].id
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition] == newList[newItemPosition]
}
