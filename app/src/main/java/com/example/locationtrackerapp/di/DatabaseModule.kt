package com.example.locationtrackerapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.locationtrackerapp.dao.LocationDao
import com.example.locationtrackerapp.model.LocationEntity
import com.example.locationtrackerapp.repository.LocationRepository
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideLocationDatabase(context: Context): LocationDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            LocationDatabase::class.java,
            "location_database"
        ).build()
    }

    @Provides
    fun provideLocationDao(database: LocationDatabase): LocationDao {
        return database.locationDao()
    }

    @Provides
    fun providePlacesClient(application: Application): PlacesClient {
        Places.initialize(application.applicationContext, "AIzaSyBSNyp6GQnnKlrMr7hD2HGiyF365tFlK5U")
        return Places.createClient(application)
    }
}
