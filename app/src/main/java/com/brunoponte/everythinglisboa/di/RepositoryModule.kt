package com.brunoponte.everythinglisboa.di

import com.brunoponte.everythinglisboa.network.IRequestService
import com.brunoponte.everythinglisboa.repository.LisbonRepository
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
    fun provideLaunchRepository(
        requestService: IRequestService
    ) : LisbonRepository {
        return LisbonRepository(requestService)
    }
}