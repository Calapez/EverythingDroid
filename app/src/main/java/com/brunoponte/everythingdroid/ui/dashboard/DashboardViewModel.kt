package com.brunoponte.everythingdroid.ui.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.brunoponte.everythingdroid.enums.DashboardOption

class DashboardViewModel : ViewModel() {

    val features: MutableLiveData<List<DashboardOption>> = MutableLiveData(listOf())

    init {
        features.value = listOf(
            DashboardOption.CAMERA,
            DashboardOption.RADARS,
            DashboardOption.START_FOREGROUND_SERVICE,
            DashboardOption.ANIMATION,
            DashboardOption.DETECT_AIRPLANE_MODE,
            DashboardOption.APP_CHOOSER,
            DashboardOption.GITHUB_REPOS,
            DashboardOption.LISBOA_ABERTA,
            DashboardOption.SERVICES)
    }

}