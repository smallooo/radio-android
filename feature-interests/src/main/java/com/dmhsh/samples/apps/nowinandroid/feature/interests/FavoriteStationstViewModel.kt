package com.dmhsh.samples.apps.nowinandroid.feature.interests

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmhsh.samples.apps.nowinandroid.core.data.repository.StationsRepo
import com.dmhsh.samples.apps.nowinandroid.core.datastore.PreferencesStore
import com.dmhsh.samples.apps.nowinandroid.core.model.data.FollowableStation
import com.dmhsh.samples.apps.nowinandroid.core.model.data.Station
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class FavoriteStationstViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val stationsRepo: StationsRepo,
    val preferences: PreferencesStore
) : ViewModel() {
    data class ForyouTabState(val titles: List<String>, val currentIndex: Int)
    val favoriteStationsState: StateFlow<StationsUiState> = combine(
        stationsRepo.getFavorite(),
        stationsRepo.getFollowedIdsStream(),
    ) { availableStations,  followedStationsIdsState ->
        StationsUiState.Stations(stations = availableStations.map { station ->
            FollowableStation(
                station = station,
                isFollowed = true
            )
        })
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = StationsUiState.Loading
    )

    fun getLocalStation() =
        suspend {
            stationsRepo.getLocalStations()
        }

    var localStationState: StateFlow<LocalStationsUiState> =
        stationsRepo.getLocalStations().map { availableStations ->
            val stations = ArrayList<Station>()
            for(item in availableStations) stations.add(item)
            LocalStationsUiState.Stations(stations1 = availableStations as ArrayList<Station>)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LocalStationsUiState.Loading
        )

    fun setFavoritedStation(station: Station) = stationsRepo.setFavorite(station)

    fun setPlayHistory(station: Station) = stationsRepo.insertOrIgnoreStation(station)
}

sealed interface StationsUiState {
    object Loading : StationsUiState

    data class Stations(
        val stations: List<FollowableStation>,
    ) : StationsUiState

    object Empty : StationsUiState
}

sealed interface LocalStationsUiState {
    object Loading : LocalStationsUiState

    data class Stations(
        val stations1: ArrayList<Station>,
    ) : LocalStationsUiState {}

    object Empty : LocalStationsUiState
}


