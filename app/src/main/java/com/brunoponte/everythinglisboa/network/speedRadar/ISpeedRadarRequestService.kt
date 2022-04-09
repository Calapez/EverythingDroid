package com.brunoponte.everythinglisboa.network.speedRadar

import com.brunoponte.everythinglisboa.network.speedRadar.model.SpeedRadarDto
import com.brunoponte.everythinglisboa.network.speedRadar.model.SpeedRadarResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ISpeedRadarRequestService {

    @GET("query")
    suspend fun getSpeedRadar(
        @Query("where") where: String,
        @Query("outFields") outFields: String,
        @Query("f") f: String,
    ) : SpeedRadarResponseDto

}