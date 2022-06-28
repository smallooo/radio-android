package com.google.samples.apps.nowinandroid.feature.foryou

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.samples.apps.nowinandroid.core.data.LocalStationsSource
import com.google.samples.apps.nowinandroid.core.data.repository.StationsRepository
import com.google.samples.apps.nowinandroid.core.database.dao.StationDao
import com.google.samples.apps.nowinandroid.core.datastore.PreferencesStore
import com.google.samples.apps.nowinandroid.core.model.data.FollowableStation
import com.google.samples.apps.nowinandroid.core.model.data.Station
import com.google.samples.apps.nowinandroid.core.navigation.Screens.QUERY_KEY
import com.google.samples.apps.nowinandroid.core.navigation.Screens.SEARCH_BACKENDS_KEY

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class  SearchListViewModel @Inject constructor(
    private val stationsRepository: StationsRepository,
    ) : ViewModel() {



    var searchRadiosState: StateFlow<StationsUiState> = combine(
        stationsRepository.getStationsByConditionList(),
        stationsRepository.getFollowedStationIdsStream(),
    ) { availableStations, followedStationsIdsState ->
        StationsUiState.Stations(stations = availableStations.map { station -> FollowableStation(station = station, isFollowed = true) })
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = StationsUiState.Loading
    )

    fun upDateSearch(){

    }

    fun setFavoritedStation(station: Station) = stationsRepository.setFavoriteStation(station)

    fun setPlayHistory(station: Station) = stationsRepository.setPlayHistory(station)
}




