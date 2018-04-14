package net.swiftzer.eric.twopanedemo.network.entities

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import net.swiftzer.eric.twopanedemo.db.entities.CachedDelivery


/**
 * Delivery list item in API server response.
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class Delivery(
        val description: String = "",
        val imageUrl: String = "",
        val location: DeliveryLocation = DeliveryLocation()
) : Parcelable {
    /**
     * Map to [CachedDelivery].
     * @param id ID to be assigned
     */
    fun toCachedDelivery(id: Int) = CachedDelivery(
            id = id,
            description = description,
            imageUrl = imageUrl,
            lat = location.lat,
            lng = location.lng,
            address = location.address
    )
}


/**
 * Delivery location in API server response.
 * @see Delivery
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class DeliveryLocation(
        val lat: Double = 0.0,
        val lng: Double = 0.0,
        val address: String = ""
) : Parcelable
