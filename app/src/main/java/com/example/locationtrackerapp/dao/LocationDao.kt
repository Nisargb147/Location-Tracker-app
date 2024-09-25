package com.example.locationtrackerapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.locationtrackerapp.model.LocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: LocationEntity)

    @Update
    suspend fun updateLocation(location: LocationEntity)

    @Delete
    suspend fun deleteLocation(location: LocationEntity)

    @Query("SELECT * FROM locations")
    suspend fun getAllLocations(): List<LocationEntity>

    @Query("SELECT * FROM locations WHERE id = :id LIMIT 1")
    suspend fun getLocationById(id: Int): LocationEntity

    @Query("SELECT * FROM locations ORDER BY distance ASC")
    fun getLocationsSortedByDistanceAsc(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM locations ORDER BY distance DESC")
    fun getLocationsSortedByDistanceDesc(): Flow<List<LocationEntity>>

}
