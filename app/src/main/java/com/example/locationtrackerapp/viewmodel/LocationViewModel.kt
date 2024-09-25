package com.example.locationtrackerapp.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.locationtrackerapp.model.CustomPlace
import com.example.locationtrackerapp.model.LocationEntity
import com.example.locationtrackerapp.repository.LocationRepository
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val repository: LocationRepository,
    private val placesClient: PlacesClient
) : ViewModel() {
    private val _locations = MutableLiveData<List<LocationEntity>>()
    val locations: LiveData<List<LocationEntity>> get() = _locations
    var isSortAscending: Boolean = true

    fun insertLocation(location: LocationEntity) = viewModelScope.launch {
        repository.insertLocation(location)
        getAllLocations()
    }

    fun updateLocation(location: LocationEntity) = viewModelScope.launch {
        repository.updateLocation(location)
    }

    fun deleteLocation(location: LocationEntity) = viewModelScope.launch {
        repository.deleteLocation(location)
    }

    fun searchLocations(query: String): LiveData<List<CustomPlace>> {
        val predictions = MutableLiveData<List<CustomPlace>>()
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
            predictions.value = response.autocompletePredictions.map { prediction ->
                CustomPlace(
                    placeId = prediction.placeId,
                    name = prediction.getPrimaryText(null).toString()
                )
            }
        }.addOnFailureListener {
            predictions.value = emptyList()
        }

        return predictions
    }

    fun getPlaceDetails(placeId: String): LiveData<CustomPlace?> {
        val placeDetails = MutableLiveData<CustomPlace?>()

        Log.d("LocationViewModel", "Requesting place details for ID: $placeId")

        val request = FetchPlaceRequest.newInstance(placeId, listOf(Place.Field.LAT_LNG, Place.Field.NAME))
        placesClient.fetchPlace(request).addOnSuccessListener { response ->
            Log.d("LocationViewModel", "Fetched place response: $response")

            val place = response.place
            if (place.id != null) {
                placeDetails.value = CustomPlace(
                    placeId = place.id,
                    name = place.name ?: "Unknown",
                    latitude = place.latLng?.latitude ?: 0.0,
                    longitude = place.latLng?.longitude ?: 0.0
                )
                Log.d("LocationViewModel", "Place details: ${placeDetails.value}")
            } else {
                Log.e("LocationViewModel", "Place ID is null")
                placeDetails.value = CustomPlace(
                    placeId = "unknown",
                    name = place.name ?: "Unknown",
                    latitude = place.latLng?.latitude ?: 0.0,
                    longitude = place.latLng?.longitude ?: 0.0
                )
            }
        }.addOnFailureListener { exception ->
            Log.e("LocationViewModel", "Failed to fetch place details: ${exception.message}")
            placeDetails.value = null
        }

        return placeDetails
    }

    fun getLocationById(id: Int): LiveData<LocationEntity> {
        return liveData {
            emit(repository.getLocationById(id))
        }
    }

    fun getAllLocations() {
        viewModelScope.launch {
            val allLocations = if (isSortAscending) {
                repository.getAllLocations().sortedWith(compareBy<LocationEntity> { it.latitude }.thenBy { it.longitude })
            } else {
                repository.getAllLocations().sortedWith(compareByDescending<LocationEntity> { it.latitude }.thenByDescending { it.longitude })
            }
            _locations.postValue(allLocations)
        }
    }

    fun toggleSortOrder(isAscending: Boolean) {
        isSortAscending = isAscending
        getAllLocations()
    }

}
