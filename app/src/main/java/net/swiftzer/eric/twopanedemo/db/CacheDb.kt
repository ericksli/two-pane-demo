package net.swiftzer.eric.twopanedemo.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import net.swiftzer.eric.twopanedemo.db.entities.CachedDelivery

/**
 * Created by Eric on 3/26/2018.
 */
@Database(
        entities = [CachedDelivery::class],
        version = 1,
        exportSchema = false
)
abstract class CacheDb : RoomDatabase() {
    companion object {
        fun create(context: Context, useInMemory:Boolean): CacheDb {
            val databaseBuilder = if (useInMemory) {
                Room.inMemoryDatabaseBuilder(context, CacheDb::class.java)
            } else {
                Room.databaseBuilder(context, CacheDb::class.java, "network-cache.db")
            }
            return databaseBuilder.fallbackToDestructiveMigration().build()
        }
    }

    abstract fun deliveries() : DeliveryDao
}