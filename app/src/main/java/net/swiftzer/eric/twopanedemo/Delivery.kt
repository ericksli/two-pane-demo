package net.swiftzer.eric.twopanedemo

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Delivery(
        val description: String = "",
        val imageUrl: String = "",
        val location: DeliveryLocation = DeliveryLocation()
) : Parcelable


@SuppressLint("ParcelCreator")
@Parcelize
data class DeliveryLocation(
        val lat: Double = 0.0,
        val lng: Double = 0.0,
        val address: String = ""
) : Parcelable
