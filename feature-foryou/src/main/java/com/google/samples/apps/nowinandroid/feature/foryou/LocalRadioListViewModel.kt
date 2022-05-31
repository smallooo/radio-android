package com.google.samples.apps.nowinandroid.feature.foryou

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalRadioListViewModel @Inject constructor(private val localStationsSource: LocalStationsSource) :

    ViewModel() { var state by mutableStateOf(
        LocalStationsContract.State(
            localStations = listOf(),
            isLoading = true
        )
    )

    var effects = Channel<CountryCategoriesContract.Effect>(UNLIMITED)
        private set

    init {
        viewModelScope.launch { getLocalStationsList() }
    }

    private suspend fun getLocalStationsList() {
        val categories = localStationsSource.getLocalStationsList("bycountry", "china")
        viewModelScope.launch {
            state = categories?.let { state.copy(localStations = it, isLoading = false) }!!
            effects.send(CountryCategoriesContract.Effect.DataWasLoaded)
        }
    }
}



