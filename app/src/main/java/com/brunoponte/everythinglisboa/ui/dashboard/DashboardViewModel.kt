package com.brunoponte.everythinglisboa.ui.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brunoponte.everythinglisboa.domain.model.Launch
import com.brunoponte.everythinglisboa.repository.LisbonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class LaunchesViewModel
@Inject
constructor(
    private val repository: LisbonRepository
) : ViewModel() {

    val launches: MutableLiveData<List<Launch>> = MutableLiveData(listOf())

    init {
        viewModelScope.launch {
            try {
                val result = repository.getLaunches()
                launches.value = result
            } catch (e: Exception) { }
        }
    }

}