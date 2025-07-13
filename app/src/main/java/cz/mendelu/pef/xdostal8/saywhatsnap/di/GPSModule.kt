package cz.mendelu.pef.xdostal8.saywhatsnap.di

import android.app.Application
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import cz.mendelu.pef.xdostal8.saywhatsnap.communication.location.DefaultLocationTracker
import cz.mendelu.pef.xdostal8.saywhatsnap.communication.location.ILocationTracker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GPSModule {

    @Provides
    @Singleton
    fun providesFusedLocationProviderClient(
        application: Application
    ): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    @Provides
    @Singleton
    fun providesLocationTracker(
        fusedLocationProviderClient: FusedLocationProviderClient,
        application: Application
    ): ILocationTracker = DefaultLocationTracker(
        fusedLocationProviderClient = fusedLocationProviderClient,
        application = application
    )
}