package net.swiftzer.eric.twopanedemo.network

import io.reactivex.Single
import net.swiftzer.eric.twopanedemo.network.entities.Delivery
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * [Retrofit] API interface for the backend server.
 */
interface DeliveryApi {
    /**
     * Get the list of deliveries.
     * @param offset pagination offset
     */
    @GET("deliveries")
    fun deliveries(@Query("offset") offset: Int? = null): Single<List<Delivery>>
}
