package com.brunoponte.everythingdroid.di

import com.brunoponte.everythingdroid.network.speedRadar.ISpeedRadarRequestService
import com.brunoponte.everythingdroid.repository.SpeedRadarRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideSpeedRadarRepository(
        speedRadarRequestService: ISpeedRadarRequestService
    ) : SpeedRadarRepository {
        return SpeedRadarRepository(speedRadarRequestService)
    }
}