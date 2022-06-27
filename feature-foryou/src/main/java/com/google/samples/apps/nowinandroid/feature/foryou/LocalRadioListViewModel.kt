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

    val _tabState = MutableStateFlow(ForyouTabState(titles = listOf("本地电台", "访问排行", "投票排行","最近更新", "正在播放", "标签", "国家", "语言", "搜索"), currentIndex = 0))

    val tabState: StateFlow<ForyouTabState> = _tabState.asStateFlow()

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


