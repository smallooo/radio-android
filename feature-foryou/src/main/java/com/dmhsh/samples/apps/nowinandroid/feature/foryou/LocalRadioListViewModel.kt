package com.dmhsh.samples.apps.nowinandroid.feature.foryou


import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmhsh.samples.apps.nowinandroid.core.data.LocalStationsSource
import com.dmhsh.samples.apps.nowinandroid.core.data.repository.StationsRepository
import com.dmhsh.samples.apps.nowinandroid.core.model.data.FollowableStation
import com.dmhsh.samples.apps.nowinandroid.core.model.data.Station

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LocalRadioListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val localStationsSource: LocalStationsSource,
    private val stationsRepository: StationsRepository,
) : ViewModel() {

    val localRadiosState: StateFlow<StationsUiState> = combine(
        stationsRepository.getAllStationsStream(),
       stationsRepository.getFollowedStationIdsStream(),
    ) { availableStations, followedStationsIdsState ->
        StationsUiState.Stations(stations = availableStations.map { station -> FollowableStation(station = station, isFollowed = true) })
    }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = StationsUiState.Loading
        )

    fun setFavoritedStation(station: Station) = stationsRepository.setFavoriteStation(station)

    fun setPlayHistory(station: Station) = stationsRepository.setPlayHistory(station)

}

sealed interface StationsUiState {
    object Loading : StationsUiState

    data class Stations(
        val stations: List<FollowableStation>,
    ) : StationsUiState

    object Empty : StationsUiState
}




