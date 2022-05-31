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
class LocalRadioListViewModel @Inject constructor( private val localStationsSource: LocalStationsSource)
    : ViewModel() {
    var state by mutableStateOf(
        LocalStationsContract.State(
            localStations = listOf(),
            isLoading = true
        )
    )

    var effects = Channel<CountryCategoriesContract.Effect>(UNLIMITED)
        private set


    fun callInit(type:String, param: String){
        viewModelScope.launch {
            getLocalStationsList(type, param)
        }
    }


     suspend fun getLocalStationsList(type:String, param: String) {
        val categories = localStationsSource.getLocalStationsList(type, param)
        viewModelScope.launch {
            state = categories?.let { state.copy(localStations = it, isLoading = false) }!!
            effects.send(CountryCategoriesContract.Effect.DataWasLoaded)
        }
    }
}



