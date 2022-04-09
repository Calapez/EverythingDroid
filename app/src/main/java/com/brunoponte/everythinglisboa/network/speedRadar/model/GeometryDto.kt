package com.brunoponte.everythinglisboa.network.speedRadar.model

import com.google.gson.annotations.SerializedName

data class GeometryDto (

    @SerializedName("coordinates")
    val coordinates: List<Double>?,

)