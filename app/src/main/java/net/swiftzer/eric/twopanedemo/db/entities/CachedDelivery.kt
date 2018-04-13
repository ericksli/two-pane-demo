package net.swiftzer.eric.twopanedemo.db.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import net.swiftzer.eric.twopanedemo.network.entities.Delivery
import net.swiftzer.eric.twopanedemo.network.entities.DeliveryLocation

/**
 * Entity class for delivery item stored in database.
 */
@Entity(tableName = "deliveries")
data class CachedDelivery(
        @PrimaryKey val id: Int,
        val description: String = "",
        val imageUrl: String = "",
        val lat: Double = 0.0,
        val lng: Double = 0.0,
        val address: String = ""
) {
    /**
     * Create [Delivery] object from [CachedDelivery].
     * @return Delivery object with values mapped from [CachedDelivery]
     */
    fun toDelivery(): Delivery {
        val location = DeliveryLocation(
                lat = lat,
                lng = lng,
                address = address
        )
        return Delivery(
                description = description,
                imageUrl = imageUrl,
                location = location
        )
    }
}
