package com.brunoponte.everythingdroid.network.speedRadar.model

import com.google.gson.annotations.SerializedName

data class SpeedRadarDto (

    @SerializedName("id")
    val id: Long?,

    @SerializedName("geometry")
    val geometry: GeometryDto?,

    @SerializedName("properties")
    val properties: PropertiesDto?,

)