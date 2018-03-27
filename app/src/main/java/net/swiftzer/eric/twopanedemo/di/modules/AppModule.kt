package net.swiftzer.eric.twopanedemo.di.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides

/**
 * Created by Eric on 3/25/2018.
 */
@Module
class AppModule {
    @Provides
    fun provideSharedPreferences(application: Application): SharedPreferences = application.getSharedPreferences("test", Context.MODE_PRIVATE)
}
