package net.swiftzer.eric.twopanedemo.db

import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import net.swiftzer.eric.twopanedemo.db.entities.CachedDelivery

/**
 * Created by Eric on 3/26/2018.
 */
@Dao
interface DeliveryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(deliveries: List<CachedDelivery>)

    @Query("SELECT * FROM deliveries ORDER BY id ASC")
    fun getDeliveries(): DataSource.Factory<Int, CachedDelivery>

    @Query("DELETE FROM deliveries")
    fun deleteAll()

    @Query("SELECT MAX(id) + 1 FROM deliveries")
    fun getNextId(): Int
}