package net.swiftzer.eric.twopanedemo.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Single
import net.swiftzer.eric.twopanedemo.db.entities.CachedDelivery

/**
 * DAO for Delivery table in database.
 */
@Dao
interface DeliveryDao {

    /**
     * Write [CachedDelivery] to database. Item with existing ID will be replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(deliveries: List<CachedDelivery>)

    /**
     * Get [CachedDelivery] from database with pagination.
     * @param limit maximum number of items returned
     * @param offset offset
     */
    @Query("SELECT * FROM deliveries ORDER BY id ASC LIMIT :limit OFFSET :offset")
    fun getDeliveriesByOffset(limit: Int, offset: Int): Single<List<CachedDelivery>>

    /**
     * Delete all items stored in database.
     */
    @Query("DELETE FROM deliveries")
    fun deleteAll()
}
