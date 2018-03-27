package net.swiftzer.eric.twopanedemo.di.components

import dagger.Subcomponent
import net.swiftzer.eric.twopanedemo.delivery.list.DeliveryListActivity
import net.swiftzer.eric.twopanedemo.delivery.list.DeliveryListFragment
import net.swiftzer.eric.twopanedemo.delivery.list.DeliveryListViewModel
import net.swiftzer.eric.twopanedemo.di.scopes.ActivityScope

/**
 * Created by eric on 26/3/2018.
 */
@ActivityScope
@Subcomponent()
interface DeliveryComponent {
    fun inject(activity: DeliveryListActivity)
    fun inject(fragment: DeliveryListFragment)
    fun inject(viewModel: DeliveryListViewModel)

    @Subcomponent.Builder
    interface Builder {
        fun build(): DeliveryComponent

//        fun deliveryListFragment(fragment: DeliveryListFragment): Builder
    }
}
