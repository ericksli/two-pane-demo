package net.swiftzer.eric.twopanedemo.delivery.detail

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.view.doOnLayout
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.delivery_detail_fragment.*
import net.swiftzer.eric.twopanedemo.GlideApp
import net.swiftzer.eric.twopanedemo.R
import net.swiftzer.eric.twopanedemo.delivery.list.DeliveryListActivity
import net.swiftzer.eric.twopanedemo.network.entities.Delivery
import org.jetbrains.anko.bundleOf

/**
 * A fragment representing a single Delivery detail screen.
 * This fragment is either contained in a [DeliveryListActivity]
 * in two-pane mode (on tablets) or a [DeliveryDetailActivity]
 * on handsets.
 */
class DeliveryDetailFragment : Fragment() {
    companion object {
        private const val ARG_DELIVERY = "delivery"
        fun newInstance(delivery: Delivery) = DeliveryDetailFragment().apply {
            arguments = bundleOf(ARG_DELIVERY to delivery)
        }
    }

    private lateinit var delivery: Delivery
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        delivery = requireNotNull(arguments?.getParcelable(ARG_DELIVERY))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.delivery_detail_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlideApp.with(this)
                .load(delivery.imageUrl)
                .placeholder(R.drawable.image_placeholder)
                .apply(RequestOptions.centerCropTransform())
                .into(thumbnail)
        title.text = delivery.description

        compositeDisposable += Observables
                .zip(
                        viewOnLayoutObservable(view),
                        viewOnLayoutObservable(footerCard),
                        googleMapObservable()
                )
                .subscribe { (contentView, footerCardView, googleMap) ->
                    with(googleMap) {
                        setPadding(0, 0, 0, contentView.height - footerCardView.y.toInt())
                        val latLng = LatLng(delivery.location.lat, delivery.location.lng)
                        val marker = MarkerOptions().position(latLng).title(delivery.location.address)
                        addMarker(marker)
                        moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f))
                    }
                }
    }

    private fun viewOnLayoutObservable(view: View) = Observable.create<View> { emitter ->
        view.doOnLayout {
            if (!emitter.isDisposed) {
                emitter.onNext(it)
                emitter.onComplete()
            }
        }
    }

    private fun googleMapObservable() = Observable.create<GoogleMap> { emitter ->
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera. In this case,
         * we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to install
         * it inside the SupportMapFragment. This method will only be triggered once the user has
         * installed Google Play services and returned to the app.
         */
        mapFragment.getMapAsync { googleMap ->
            if (!emitter.isDisposed) {
                emitter.onNext(googleMap)
                emitter.onComplete()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}
