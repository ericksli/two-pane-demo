package net.swiftzer.eric.twopanedemo.network

import io.reactivex.Single
import net.swiftzer.eric.twopanedemo.Delivery
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Eric on 3/25/2018.
 */
interface DeliveryApi {
    @GET("deliveries")
    fun deliveries(@Query("offset") offset: Int): Single<List<Delivery>>
}