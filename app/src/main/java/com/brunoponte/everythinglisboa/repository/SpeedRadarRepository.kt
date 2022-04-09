package com.brunoponte.everythinglisboa.repository

import com.brunoponte.everythinglisboa.domain.speedRadar.model.SpeedRadar
import com.brunoponte.everythinglisboa.network.speedRadar.ISpeedRadarRequestService

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

    /*
    suspend fun getCompanyInfo(): CompanyInfo? {

        try{
            val companyInfo = getCompanyInfoFromNetwork()

            // insert into cache
            companyInfoDao.insertCompanyInfo(CompanyInfoEntityMapper.mapFromDomainModel(companyInfo))
        } catch (e: Exception){
            // There was a network issue
            e.printStackTrace()
        }

        // query the cache
        val cachedCompanyInfoEntity = companyInfoDao.getCompanyInfo()

        val companyInfo = cachedCompanyInfoEntity?.let {
            CompanyInfoEntityMapper.mapToDomainModel(it)
        }

        return companyInfo
    }


    override suspend fun getLaunches(
        limit: Int,
        offset: Int,
        launchYear: Int?,
        launchSuccess: Boolean?,
        sortAsc: Boolean?,
        order: String
    ): List<Launch> {
        try {
            val launches = getLaunchesFromNetwork(
                limit,
                offset,
                launchYear,
                launchSuccess,
                sortAsc,
                order
            )

            // insert into cache
            launchDao.insertLaunches(LaunchEntityMapper.toEntityList(launches))
        } catch (e: Exception) {
            // There was a network issue
            e.printStackTrace()
        }

        // query the cache
        val cachesLaunchesEntity = if (sortAsc == false) launchDao.getLaunchesSortedDesc(
            limit = limit,
            offset = offset)
        else launchDao.getLaunchesSortedAsc(
            limit = limit,
            offset = offset)

        return LaunchEntityMapper.fromEntityList(cachesLaunchesEntity)
    }

    override suspend fun clearLaunches() {
        launchDao.nukeTable()

    }

    // WARNING: This will throw exception if there is no network connection
    private suspend fun getCompanyInfoFromNetwork() =
        CompanyInfoDtoMapper.mapToDomainModel(
            requestService.getCompanyInfo())

    // WARNING: This will throw exception if there is no network connection
    private suspend fun getLaunchesFromNetwork(
        limit: Int,
        offset: Int,
        launchYear: Int?,
        launchSuccess: Boolean?,
        sortAsc: Boolean?,
        order: String
    ) = LaunchDtoMapper.toDomainList(
        requestService.getLaunches(
            limit,
            offset,
            launchYear,
            launchSuccess,
            order,
            if (sortAsc == false) "desc" else "asc")
    )
*/
}