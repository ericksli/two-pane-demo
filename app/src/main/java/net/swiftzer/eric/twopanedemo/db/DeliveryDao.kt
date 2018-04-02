package net.swiftzer.eric.twopanedemo.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Single
import net.swiftzer.eric.twopanedemo.db.entities.CachedDelivery

/**
 * Created by Eric on 3/26/2018.
 */
@Dao
interface DeliveryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(deliveries: List<CachedDelivery>)

    @Query("SELECT * FROM deliveries ORDER BY id ASC LIMIT :limit OFFSET :offset")
    fun getDeliveriesByOffset(limit: Int, offset: Int): Single<List<CachedDelivery>>

    @Query("DELETE FROM deliveries")
    fun deleteAll()
}
