package com.brunoponte.everythinglisboa.network.speedRadar.model

import com.google.gson.annotations.SerializedName

data class SpeedRadarResponseDto (

    @SerializedName("features")
    val speedRadars: List<SpeedRadarDto>?,

)