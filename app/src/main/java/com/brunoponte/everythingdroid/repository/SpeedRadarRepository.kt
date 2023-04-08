package com.brunoponte.everythingdroid.repository

import com.brunoponte.everythingdroid.domain.speedRadar.model.SpeedRadar
import com.brunoponte.everythingdroid.network.speedRadar.ISpeedRadarRequestService

class SpeedRadarRepository(
    private val speedRadarRequestService: ISpeedRadarRequestService,
    //private val lisbonEntryDao: LisbonEntryDao,
) {

    suspend fun getSpeedRadars(): List<SpeedRadar> {
        try {
            val speedRadars = speedRadarRequestService.getSpeedRadar(
                "1=1",
                "*",
                "pgeojson"
            ).speedRadars

            return speedRadars?.map {
                SpeedRadar(
                    it.id,
                    it.geometry?.coordinates?.get(1),
                    it.geometry?.coordinates?.get(0),
                    it.properties?.angle)
            } ?: listOf()
        } catch (e: Exception) {
            // There was a network issue
            e.printStackTrace()
            return listOf()
        }
    }
}