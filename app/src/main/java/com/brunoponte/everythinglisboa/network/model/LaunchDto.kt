package com.brunoponte.everythinglisboa.network.model

import com.google.gson.annotations.SerializedName

data class LaunchDto (

    @SerializedName("flight_number")
    val flightNumber: Int?,

    @SerializedName("mission_name")
    val missionName: String?,

    @SerializedName("launch_date_unix")
    val launchDateUnix: Long?,

    @SerializedName("launch_success")
    val launchSuccess: Boolean?,

)