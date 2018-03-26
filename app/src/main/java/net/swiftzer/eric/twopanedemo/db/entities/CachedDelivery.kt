package net.swiftzer.eric.twopanedemo.db.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "deliveries")
data class CachedDelivery(
        @PrimaryKey val id: Int,
        val description: String,
        val imageUrl: String,
        val lat: Double,
        val lng: Double,
        val address: String
)
