package com.brunoponte.everythinglisboa.network

import com.brunoponte.everythinglisboa.network.model.LaunchDto
import retrofit2.http.GET
import retrofit2.http.Query

interface IRequestService {

    @GET("launches")
    suspend fun getLaunches() : List<LaunchDto>

}