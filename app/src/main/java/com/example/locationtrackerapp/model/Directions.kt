package com.example.locationtrackerapp.model

import com.google.gson.annotations.SerializedName

data class Directions(
    @field:SerializedName("status")
    var status: String? = null,
    @field:SerializedName("error_message")
    var errorMessage: String? = null,
    @field:SerializedName("geocoded_waypoints")
    var geocodedWaypoints: List<GeocodedWaypoints>? = null,
    @field:SerializedName("routes")
    var routes: List<Routes>? = null
)

data class GeocodedWaypoints(
    @field:SerializedName("geocoder_status")
    var geocoderStatus: String? = null,
    @field:SerializedName("place_id")
    var placeId: String? = null,
    @field:SerializedName("types")
    var types: List<String>? = null
)

data class Routes(
    @field:SerializedName("bounds")
    var bounds: Bounds? = null,
    @field:SerializedName("copyrights")
    var copyrights: String? = null,
    @field:SerializedName("overview_polyline")
    var overviewPolyline: OverviewPolyline? = null,
    @field:SerializedName("summary")
    var summary: String? = null,
    @field:SerializedName("legs")
    var legs: List<Legs>? = null,
    @field:SerializedName("warnings")
    var warnings: List<String>? = null,
    @field:SerializedName("waypoint_order")
    var waypointOrder: List<String>? = null
)

data class Bounds(
    @field:SerializedName("northeast")
    var northeast: Northeast? = null,
    @field:SerializedName("southwest")
    var southwest: Southwest? = null
)

data class Northeast(
    @field:SerializedName("lat")
    var lat: Double = 0.toDouble(),
    @field:SerializedName("lng")
    var lng: Double = 0.toDouble()
)

data class Southwest(
    @field:SerializedName("lat")
    var lat: Double = 0.toDouble(),
    @field:SerializedName("lng")
    var lng: Double = 0.toDouble()
)

data class OverviewPolyline(
    @field:SerializedName("points")
    var points: String? = null
)

data class Legs(
    @field:SerializedName("distance")
    var distance: Distance? = null,
    @field:SerializedName("duration")
    var duration: Duration? = null,
    @field:SerializedName("end_address")
    var endAddress: String? = null,
    @field:SerializedName("end_location")
    var endLocation: EndLocation? = null,
    @field:SerializedName("start_address")
    var startAddress: String? = null,
    @field:SerializedName("start_location")
    var startLocation: StartLocation? = null,
    @field:SerializedName("steps")
    var steps: List<Steps>? = null,
    @field:SerializedName("traffic_speed_entry")
    var trafficSpeedEntry: List<String>? = null,
    @field:SerializedName("via_waypoint")
    var viaWaypoint: List<String>? = null
)

data class Distance(
    @field:SerializedName("text")
    var text: String? = null,
    @field:SerializedName("value")
    var value: Int = 0
)

data class Duration(
    @field:SerializedName("text")
    var text: String? = null,
    @field:SerializedName("value")
    var value: Int = 0
)

data class EndLocation(
    @field:SerializedName("lat")
    var lat: Double = 0.toDouble(),
    @field:SerializedName("lng")
    var lng: Double = 0.toDouble()
)

data class StartLocation(
    @field:SerializedName("lat")
    var lat: Double = 0.toDouble(),
    @field:SerializedName("lng")
    var lng: Double = 0.toDouble()
)

data class Steps(
    @field:SerializedName("distance")
    var distance: Distance? = null,
    @field:SerializedName("duration")
    var duration: Duration? = null,
    @field:SerializedName("end_location")
    var endLocation: EndLocation? = null,
    @field:SerializedName("html_instructions")
    var htmlInstructions: String? = null,
    @field:SerializedName("polyline")
    var polyline: DirectionPolyline? = null,
    @field:SerializedName("start_location")
    var startLocation: StartLocation? = null,
    @field:SerializedName("travel_mode")
    var travelMode: String? = null,
    @field:SerializedName("maneuver")
    var maneuver: String? = null
)

data class DirectionPolyline(
    @field:SerializedName("points")
    var points: String? = null
)

