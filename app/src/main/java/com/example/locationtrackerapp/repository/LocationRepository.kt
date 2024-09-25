package com.example.locationtrackerapp.repository

import com.example.locationtrackerapp.dao.LocationDao
import com.example.locationtrackerapp.model.LocationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationRepository @Inject constructor(private val locationDao: LocationDao) {
    suspend fun insertLocation(location: LocationEntity) = locationDao.insertLocation(location)
    suspend fun updateLocation(location: LocationEntity) = locationDao.updateLocation(location)
    suspend fun deleteLocation(location: LocationEntity) = locationDao.deleteLocation(location)
    suspend fun getAllLocations(): List<LocationEntity> {
        return locationDao.getAllLocations()
    }
    suspend fun getLocationById(id: Int): LocationEntity {
        return locationDao.getLocationById(id)
    }
}

