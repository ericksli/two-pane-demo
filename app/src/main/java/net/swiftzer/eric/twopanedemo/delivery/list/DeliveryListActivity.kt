package net.swiftzer.eric.twopanedemo.delivery.list

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.delivery_list_activity.*
import net.swiftzer.eric.twopanedemo.R
import net.swiftzer.eric.twopanedemo.TwoPaneApplication
import net.swiftzer.eric.twopanedemo.delivery.detail.DeliveryDetailActivity
import net.swiftzer.eric.twopanedemo.delivery.detail.DeliveryDetailFragment
import net.swiftzer.eric.twopanedemo.network.entities.Delivery
import net.swiftzer.eric.twopanedemo.viewModelOf
import org.jetbrains.anko.toast
import javax.inject.Inject

/**
 * An activity representing a list of delivery items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [DeliveryDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class DeliveryListActivity : AppCompatActivity() {
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    @Inject
    internal lateinit var deliveryListRepository: DeliveryListRepository
    private lateinit var viewModel: DeliveryListActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val component = TwoPaneApplication.instance.appComponent
                .deliveryBuilder()
                .build()
        component.inject(this)

        viewModel = viewModelOf(DeliveryListActivityViewModel.Factory(deliveryListRepository))

        setContentView(R.layout.delivery_list_activity)

        // The detail container view will be present only in the
        // large-screen layouts (res/values-w900dp).
        // If this view is present, then the
        // activity should be in two-pane mode.
        twoPane = detailContainer != null

        setSupportActionBar(toolbar)
        setTitle(if (twoPane) R.string.app_name else R.string.delivery_list_title)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.listContainer, DeliveryListFragment.newInstance(this::onDeliverySelected))
                    .commit()
        } else {
            val listFragment = supportFragmentManager.findFragmentById(R.id.listContainer) as DeliveryListFragment
            listFragment.onItemClickedCallback = this::onDeliverySelected
        }

        viewModel.clearCacheLiveData.observe(this, Observer {
            it?.let {
                toast(R.string.delivery_list_clear_cache_success)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.delivery, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.clearCache -> {
            viewModel.clearLocalCache()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    /**
     * Callback when list item is selected by user.
     *
     * Start a new [DeliveryDetailActivity] when in small screen or show the
     * [DeliveryDetailFragment] in two-pane mode.
     *
     * @param delivery the selected delivery item
     */
    private fun onDeliverySelected(delivery: Delivery) {
        if (twoPane) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.detailContainer, DeliveryDetailFragment.newInstance(delivery))
                    .commit()
        } else {
            DeliveryDetailActivity.startActivity(this, delivery)
        }
    }
}
