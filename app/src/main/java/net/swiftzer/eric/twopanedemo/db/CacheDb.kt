package net.swiftzer.eric.twopanedemo.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import net.swiftzer.eric.twopanedemo.db.entities.CachedDelivery

/**
 * Database to store the cached delivery items.
 */
@Database(
        entities = [CachedDelivery::class],
        version = 1,
        exportSchema = false
)
abstract class CacheDb : RoomDatabase() {
    companion object {
        /**
         * Create database instance.
         * @param context application context
         * @param useInMemory in-memory DB is created when it is true
         * @return Room database instance
         */
        fun create(context: Context, useInMemory: Boolean): CacheDb {
            val databaseBuilder = if (useInMemory) {
                Room.inMemoryDatabaseBuilder(context, CacheDb::class.java)
            } else {
                Room.databaseBuilder(context, CacheDb::class.java, "network-cache.db")
            }
            return databaseBuilder.fallbackToDestructiveMigration().build()
        }
    }

    /**
     * Obtain [DeliveryDao].
     * @return DAO instance
     */
    abstract fun deliveries(): DeliveryDao
}
