package net.swiftzer.eric.twopanedemo.di.modules

import android.app.Application
import dagger.Module
import dagger.Provides
import net.swiftzer.eric.twopanedemo.db.CacheDb
import net.swiftzer.eric.twopanedemo.db.DeliveryDao
import javax.inject.Singleton

/**
 * Room DB module.
 */
@Module
class DbModule {
    @Singleton
    @Provides
    fun provideDb(application: Application): CacheDb = CacheDb.create(application, false)

    @Provides
    fun provideDeliveryDao(db: CacheDb): DeliveryDao = db.deliveries()
}
