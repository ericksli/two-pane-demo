package net.swiftzer.eric.twopanedemo

import android.app.Application
import timber.log.Timber

/**
 * Created by Eric on 3/25/2018.
 */
class TwoPaneApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Timber
        Timber.plant(Timber.DebugTree())
    }
}