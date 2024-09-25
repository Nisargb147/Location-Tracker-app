package com.example.locationtrackerapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.locationtrackerapp.R
import com.example.locationtrackerapp.databinding.FragmentMapBinding
import com.example.locationtrackerapp.model.Directions
import com.example.locationtrackerapp.model.LocationEntity
import com.example.locationtrackerapp.model.Routes
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.google.maps.android.PolyUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : BaseFragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentMapBinding
    private lateinit var googleMap: GoogleMap
    private var polyline: Polyline? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        observeData()
    }

    private fun showMarkersAndDrawPath(locationList: List<LocationEntity>) {
        Log.d("MapFragment", "showMarkersAndDrawPath called with locations: $locationList")

        googleMap.clear()

        // Add markers for each location
        locationList.forEach { location ->
            val latLng = LatLng(location.latitude, location.longitude)
            googleMap.addMarker(MarkerOptions().position(latLng).title(location.name))
        }

        // Zoom
        val builder = LatLngBounds.Builder()
        locationList.forEach { builder.include(LatLng(it.latitude, it.longitude)) }
        val bounds = builder.build()
        val padding = 100
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        googleMap.animateCamera(cameraUpdate)

        //Polyline draw
        drawPathForAllLocations(locationList)
    }

    private fun drawPathForAllLocations(locationList: List<LocationEntity>) {
        if (locationList.size < 2) {
            Log.d("MapFragment", "Not enough locations to draw a path.")
            return
        }
        val waypoints = locationList.map { LatLng(it.latitude, it.longitude) }
        fetchDirectionsForAllWaypoints(waypoints)
    }

    private fun fetchDirectionsForAllWaypoints(waypoints: List<LatLng>) {
        val origin = waypoints.first()
        val destination = waypoints.last()
        val waypointsString = waypoints.subList(1, waypoints.size - 1)
            .joinToString("|") { "${it.latitude},${it.longitude}" }

        val pathUrl = getDirectionsUrl(origin, destination, waypointsString)

        pathDraw.downloadTaskExecute(pathUrl) { response ->
            Log.d("MapFragment", "Directions API response: $response")

            val directions: Directions? = Gson().fromJson(response.toString(), Directions::class.java)
            if (directions?.routes?.isNotEmpty() == true) {
                Log.d("MapFragment", "Routes found in API response: ${directions.routes}")
                updatePolyline(directions.routes!!)
            } else {
                Log.e("MapFragment", "No routes found in directions API response.")
            }
        }
    }

    private fun getDirectionsUrl(origin: LatLng, destination: LatLng, waypoints: String): String {
        val str_origin = "origin=${origin.latitude},${origin.longitude}"
        val str_dest = "destination=${destination.latitude},${destination.longitude}"
        val str_waypoints = "waypoints=$waypoints"
        val parameters = "$str_origin&$str_dest&$str_waypoints&sensor=false&units=metric&mode=driving&alternatives=false"
        val output = "json"
        val apiKey = "&key=AIzaSyBSNyp6GQnnKlrMr7hD2HGiyF365tFlK5U"

        val url = "https://maps.googleapis.com/maps/api/directions/$output?$parameters$apiKey"
        Log.d("MapFragment", "Generated directions URL: $url")
        return url
    }

    private fun updatePolyline(routes: List<Routes>) {
        val polylinePoints = mutableListOf<LatLng>()
        routes.forEach { route ->
            route.legs?.forEach { leg ->
                leg.steps?.forEach { step ->
                    val decodedPoints = PolyUtil.decode(step.polyline?.points ?: "")
                    polylinePoints.addAll(decodedPoints)
                }
            }
        }

        if (polylinePoints.isEmpty()) {
            Log.e("MapFragment", "No points decoded for polyline.")
        } else {
            drawPath(polylinePoints)
        }
    }

    private fun drawPath(pathPoints: List<LatLng>) {
        polyline?.remove()

        if (pathPoints.isNotEmpty()) {
            polyline = googleMap.addPolyline(
                PolylineOptions()
                    .addAll(pathPoints)
                    .width(10f)
                    .color(resources.getColor(R.color.colorPrimary, null))
                    .startCap(RoundCap())
                    .endCap(RoundCap())
                    .geodesic(true)
            )
            Log.d("MapFragment", "Polyline drawn with points: $pathPoints")
        } else {
            Log.e("MapFragment", "No path points to draw.")
        }
    }

    private fun observeData(){
        viewModel.locations.observe(viewLifecycleOwner, Observer { locationList ->
            if (locationList != null && locationList.isNotEmpty()) {
                Log.d("MapFragment", "Locations received: $locationList")
                showMarkersAndDrawPath(locationList)
            } else {
                Log.d("MapFragment", "No locations found or empty list.")
            }
        })

        viewModel.getAllLocations()
    }
}
