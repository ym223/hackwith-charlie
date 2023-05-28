package jp.chocofac.charlie.data.service.location

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocationService {

    @Singleton
    @Provides
    fun provideLocationProvider(@ApplicationContext appContext: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(appContext)
    }

    @Singleton
    @Provides
    fun provideLocationRepositoryProvider(fusedLocationProviderClient: FusedLocationProviderClient): LocationRepository {
        return LocationRepository(fusedLocationProviderClient)
    }
}