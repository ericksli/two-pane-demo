package net.swiftzer.eric.twopanedemo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.delivery_list_activity.*

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [DeliveryDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class DeliveryListActivity : BaseActivity() {
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.delivery, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.clearCache -> {
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

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