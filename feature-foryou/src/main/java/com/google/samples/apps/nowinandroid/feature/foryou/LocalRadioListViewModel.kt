package com.google.samples.apps.nowinandroid.feature.foryou


import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.samples.apps.nowinandroid.core.data.LocalStationsSource
import com.google.samples.apps.nowinandroid.core.data.repository.StationsRepository
import com.google.samples.apps.nowinandroid.core.model.data.FollowableStation
import com.google.samples.apps.nowinandroid.core.model.data.Station

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
    data class ForyouTabState(val titles: List<String>, val currentIndex: Int)
//    val _tabState = MutableStateFlow(ForyouTabState(titles = listOf("本地电台", "访问排行", "投票排行","最近更新", "正在播放", "标签", "国家", "语言", "搜索"), currentIndex = 0))
//    val tabState: StateFlow<ForyouTabState> = _tabState.asStateFlow()

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


    val topVoteRadiosState: StateFlow<StationsUiState> = combine(
        stationsRepository.gettopVotedStationsStream(),
        stationsRepository.getFollowedStationIdsStream(),
    ) { availableStations1, followedStationsIdsState ->
        StationsUiState.Stations(stations = availableStations1.map { station -> FollowableStation(station = station, isFollowed = true) })
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = StationsUiState.Loading
    )

    val lateUpdateRadiosState: StateFlow<StationsUiState> = combine(
        stationsRepository.getLateUpdateStationsStream(),
        stationsRepository.getFollowedStationIdsStream(),
    ) { availableStations2, followedStationsIdsState ->
        StationsUiState.Stations(stations = availableStations2.map { station -> FollowableStation(station = station, isFollowed = true) })
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = StationsUiState.Loading
    )

    val nowPlayingRadiosState: StateFlow<StationsUiState> = combine(
        stationsRepository.getnowPlayingStationsStream(),
        stationsRepository.getFollowedStationIdsStream(),
    ) { availableStations3, followedStationsIdsState ->
        StationsUiState.Stations(stations = availableStations3.map { station -> FollowableStation(station = station, isFollowed = true) })
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = StationsUiState.Loading
    )



    //val setFavoritedStation = stationsRepository.setFavoriteStation(stationUUID)
     fun setFavoritedStation(station: Station) = stationsRepository.setFavoriteStation(station)
//        viewModelScope.launch {
//            stationsRepository.setFavoriteStation(stationUUID)
//        }
    fun setPlayHistory(station: Station) = stationsRepository.setPlayHistory(station)
    val topVisitRadiosState: Flow<List<Station>> = stationsRepository.getTopVisitedStationsStream()


}

sealed interface StationsUiState {
    object Loading : StationsUiState

    data class Stations(
        val stations: List<FollowableStation>,
    ) : StationsUiState

    object Empty : StationsUiState
}

sealed interface StationsUiState1 {
    object Loading : StationsUiState1

    data class Stations(
        val stations: List<FollowableStation>,
    ) : StationsUiState1

    object Empty : StationsUiState1
}

sealed interface StationsUiState2 {
    object Loading : StationsUiState2

    data class Stations(
        val stations: List<FollowableStation>,
    ) : StationsUiState2

    object Empty : StationsUiState2
}

sealed interface StationsUiState3 {
    object Loading : StationsUiState3

    data class Stations(
        val stations: List<FollowableStation>,
    ) : StationsUiState3

    object Empty : StationsUiState3
}

sealed interface StationsUiState4 {
    object Loading : StationsUiState4

    data class Stations(
        val stations: List<FollowableStation>,
    ) : StationsUiState4

    object Empty : StationsUiState4
}

sealed interface StationsUiState5 {
    object Loading : StationsUiState5

    data class Stations(
        val stations: List<FollowableStation>,
    ) : StationsUiState5

    object Empty : StationsUiState5
}


