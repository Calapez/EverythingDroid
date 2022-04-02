package com.brunoponte.everythinglisboa.domain.model

data class Launch(
    val flightNumber: Int?,
    val missionName: String?,
    val launchDateUnix: Long?,
    val launchSuccess: Boolean?,
)