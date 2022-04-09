package com.brunoponte.everythinglisboa.di

import com.brunoponte.everythinglisboa.network.speedRadar.ISpeedRadarRequestService
import com.brunoponte.everythinglisboa.repository.SpeedRadarRepository
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