package net.swiftzer.eric.twopanedemo.delivery.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.delivery_detail_activity.*
import net.swiftzer.eric.twopanedemo.BaseActivity
import net.swiftzer.eric.twopanedemo.delivery.list.DeliveryListActivity
import net.swiftzer.eric.twopanedemo.R
import net.swiftzer.eric.twopanedemo.network.entities.Delivery
import org.jetbrains.anko.startActivity

/**
 * An activity representing a single Delivery detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [DeliveryListActivity].
 */
class DeliveryDetailActivity : BaseActivity() {
    companion object {
        private const val EXTRA_DELIVERY = "delivery"
        fun startActivity(context: Context, delivery: Delivery) {
            context.startActivity<DeliveryDetailActivity>(EXTRA_DELIVERY to delivery)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.delivery_detail_activity)

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setTitle(R.string.delivery_details_title)
            setDisplayHomeAsUpEnabled(true)
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            val delivery = requireNotNull(intent.getParcelableExtra<Delivery>(EXTRA_DELIVERY))
            val fragment = DeliveryDetailFragment.newInstance(delivery)
            supportFragmentManager.beginTransaction()
                    .replace(R.id.detailContainer, fragment)
                    .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back

            navigateUpTo(Intent(this, DeliveryListActivity::class.java))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
