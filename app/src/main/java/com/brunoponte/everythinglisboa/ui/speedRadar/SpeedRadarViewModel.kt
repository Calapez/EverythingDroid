package com.brunoponte.everythinglisboa.ui.speedRadar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brunoponte.everythinglisboa.domain.speedRadar.model.SpeedRadar
import com.brunoponte.everythinglisboa.repository.SpeedRadarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SpeedRadarViewModel
@Inject
constructor(
    private val repository: SpeedRadarRepository
) : ViewModel() {

    val speedRadars: MutableLiveData<List<SpeedRadar>> = MutableLiveData(listOf())

    init {
        viewModelScope.launch {
            try {
                val result = repository.getSpeedRadars()
                speedRadars.value = result
            } catch (e: Exception) { }
        }
    }

}