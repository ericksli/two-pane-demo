package net.swiftzer.eric.twopanedemo.di.components

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import net.swiftzer.eric.twopanedemo.di.modules.AppModule
import net.swiftzer.eric.twopanedemo.di.modules.NetworkModule
import javax.inject.Singleton

/**
 * Created by Eric on 3/25/2018.
 */
@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {
    fun deliveryBuilder(): DeliveryComponent.Builder

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun application(application: Application): Builder

        fun networkModule(networkModule: NetworkModule): Builder
    }
}
