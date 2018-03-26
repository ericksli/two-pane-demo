package net.swiftzer.eric.twopanedemo

import android.app.Application
import net.swiftzer.eric.twopanedemo.di.components.AppComponent
import net.swiftzer.eric.twopanedemo.di.components.DaggerAppComponent
import net.swiftzer.eric.twopanedemo.di.modules.NetworkModule
import timber.log.Timber

/**
 * Created by Eric on 3/25/2018.
 */
class TwoPaneApplication : Application() {
    companion object {
        lateinit var instance: TwoPaneApplication
            internal set
    }

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        instance = this

        // Timber
        Timber.plant(Timber.DebugTree())

        appComponent = DaggerAppComponent.builder()
                .application(this)
                .networkModule(NetworkModule(BuildConfig.API_ENDPOINT))
                .build()
    }
}
