package net.swiftzer.eric.twopanedemo.di.modules

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

/**
 * Created by Eric on 3/25/2018.
 */
@Module
class AppModule(private val application: Application) {
    @Provides
    fun provideApplication() = application

    @Provides
    fun provideSharedPreferences() = application.getSharedPreferences("test", Context.MODE_PRIVATE)
}