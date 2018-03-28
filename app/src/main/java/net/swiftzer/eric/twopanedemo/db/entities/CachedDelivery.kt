package net.swiftzer.eric.twopanedemo.db.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import net.swiftzer.eric.twopanedemo.network.entities.Delivery
import net.swiftzer.eric.twopanedemo.network.entities.DeliveryLocation

@Entity(tableName = "deliveries")
data class CachedDelivery(
        @PrimaryKey val id: Int,
        val description: String,
        val imageUrl: String,
        val lat: Double,
        val lng: Double,
        val address: String
) {
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
