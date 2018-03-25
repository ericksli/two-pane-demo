package net.swiftzer.eric.twopanedemo.di.components

import dagger.Component
import net.swiftzer.eric.twopanedemo.DeliveryListActivity
import net.swiftzer.eric.twopanedemo.di.modules.AppModule
import javax.inject.Singleton

/**
 * Created by Eric on 3/25/2018.
 */
@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(activity: DeliveryListActivity)
}