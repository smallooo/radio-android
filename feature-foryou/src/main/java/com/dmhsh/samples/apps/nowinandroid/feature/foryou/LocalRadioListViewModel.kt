package com.dmhsh.samples.apps.nowinandroid.feature.foryou


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmhsh.samples.apps.nowinandroid.core.data.NetSource
import com.dmhsh.samples.apps.nowinandroid.core.data.repository.StationsRepo
import com.dmhsh.samples.apps.nowinandroid.core.model.data.Station

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class LocalRadioListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val netSource: NetSource,
    private val stationsRepo: StationsRepo,
) : ViewModel() {

    val localRadiosState: StateFlow<StationsUiState> = combine(
        stationsRepo.getAllStream(),
    ) { availableStations ->
        StationsUiState.Stations(stations = availableStations.get(0) )
    }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = StationsUiState.Loading
        )

    fun setFavoritedStation(station: Station) = stationsRepo.setFavorite(station)

    fun setPlayHistory(station: Station) = stationsRepo.setPlayHistory(station)

}

sealed interface StationsUiState {
    object Loading : StationsUiState

    data class Stations(
        val stations: List<Station>,
    ) : StationsUiState {

    }

    object Empty : StationsUiState
}




