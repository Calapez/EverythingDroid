package com.brunoponte.everythingdroid.ui.speedRadar

import android.Manifest
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.brunoponte.everythingdroid.BuildConfig
import com.brunoponte.everythingdroid.R
import com.brunoponte.everythingdroid.databinding.FragmentSpeedRadarBinding
import com.brunoponte.everythingdroid.domain.speedRadar.model.SpeedRadar
import com.brunoponte.everythingdroid.helpers.Util
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.Marker

private val permissions = listOf(Manifest.permission.ACCESS_FINE_LOCATION)
private val permissionRequestCode = 200

@AndroidEntryPoint
class SpeedRadarFragment : Fragment() {

    private lateinit var binding: FragmentSpeedRadarBinding

    private val viewModel: SpeedRadarViewModel by viewModels()
    private var markers = mutableListOf<Marker>()
    private var answeredToPermissions = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        Configuration.getInstance().load(requireContext(),
            PreferenceManager.getDefaultSharedPreferences(requireContext()))
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSpeedRadarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.map.setTileSource(TileSourceFactory.MAPNIK)
    }

    override fun onResume() {
        super.onResume()

        if (answeredToPermissions || Util.hasPermissions(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            binding.map.onResume()
            setupMap()

            binding.map.addOnFirstLayoutListener { v, left, top, right, bottom ->
                setupViewModelObservers()
            }
        } else {
            requestPermissions(permissions.toTypedArray(), permissionRequestCode)
        }
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionRequestCode) {
            answeredToPermissions = true
        }
    }

    private fun setupViewModelObservers() {
        viewModel.speedRadars.observe(viewLifecycleOwner) { speedRadars ->
            updateMapSpeedRadars(speedRadars)
            moveCameraToMarkers(speedRadars)
        }
    }

    private fun setupMap() {
        binding.map.let { map ->
            map.setTileSource(TileSourceFactory.MAPNIK)
            map.setMultiTouchControls(true)
            map.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        }
    }

    private fun updateMapSpeedRadars(speedRadars: List<SpeedRadar>?) {
        markers.clear()

        speedRadars?.forEach { speedRadar ->

            if (speedRadar.lat == null || speedRadar.lon == null) {
                return@forEach
            }

            val marker = Marker(binding.map).also {
                it.id = speedRadar.id.toString()
                it.position = GeoPoint(speedRadar.lat, speedRadar.lon)
                it.icon = BitmapDrawable(
                    requireContext().resources,
                    Util.bitmapResize(
                        Util.getBitmapFromDrawable(
                            requireContext(),
                            R.drawable.ic_location_arrow,
                            speedRadar.angle),
                        0.4f))
                it.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
            }

            // Do nothing on click
            marker.setOnMarkerClickListener { _, _ -> true }

            markers.add(marker)
        }

        // Update map overlays
        binding.map.apply {
            overlays.clear()
            overlays.addAll(markers)
            invalidate()
        }
    }

    private fun moveCameraToMarkers(speedRadars: List<SpeedRadar>) {
        var minLat = Int.MAX_VALUE.toDouble()
        var maxLat = Int.MIN_VALUE.toDouble()
        var minLong = Int.MAX_VALUE.toDouble()
        var maxLong = Int.MIN_VALUE.toDouble()

        var counter = 0
        for (speedRadar in speedRadars) {
            counter++
            val lat: Double = speedRadar.lat ?: 0.0
            val lon: Double = speedRadar.lon ?: 0.0
            if (lat < minLat) minLat = lat
            if (lat > maxLat) maxLat = lat
            if (lon < minLong) minLong = lon
            if (lon > maxLong) maxLong = lon
        }

        if (counter == 0)
            return

        val boundingBox = BoundingBox(maxLat, maxLong, minLat, minLong)
        binding.map.apply {
            zoomToBoundingBox(boundingBox.increaseByScale(1.3f), true)
            invalidate()
        }
    }
}