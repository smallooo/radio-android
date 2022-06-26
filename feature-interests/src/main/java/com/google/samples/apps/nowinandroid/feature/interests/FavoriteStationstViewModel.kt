package com.google.samples.apps.nowinandroid.feature.interests


import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.samples.apps.nowinandroid.core.data.LocalStationsSource
import com.google.samples.apps.nowinandroid.core.data.repository.StationsRepository
import com.google.samples.apps.nowinandroid.core.model.data.FollowableStation
import com.google.samples.apps.nowinandroid.core.model.data.Station

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class FavoriteStationstViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    stationsRepository: StationsRepository
) : ViewModel() {

    data class ForyouTabState(val titles: List<String>, val currentIndex: Int)

    val _tabState = MutableStateFlow(
        ForyouTabState(
            titles = listOf(
                "本地电台",
                "访问排行",
                "投票排行",
                "最近更新",
                "正在播放",
                "标签",
                "国家",
                "语言",
                "搜索"
            ), currentIndex = 0
        )
    )

    val tabState: StateFlow<ForyouTabState> = _tabState.asStateFlow()

    val favoriteStationsState: StateFlow<StationsUiState> = combine(
        stationsRepository.getFavoriteStations(),
        stationsRepository.getFollowedStationIdsStream(),
    ) { availableStations, followedStationsIdsState ->
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



    val favoriteStationsState1 : StateFlow<StationsUiState> = combine(stationsRepository.getStationbyIdsEntitiesStream(
        listOf<String>(
            "53aecd24-34ad-40ab-b74a-64bd47a119d7",
        "37d1f4fb-0812-432d-af30-e91ca28ae05c",
            "189ccb04-13fb-48e6-9adb-49e1c0fa1c6a"
        ).toList()),
        stationsRepository.getFollowedStationIdsStream()
    ){ availableStations, followedStationsIdsState ->
        StationsUiState.Stations(stations = availableStations.map { station -> FollowableStation(station = station, isFollowed = true) })
    }
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = StationsUiState.Loading
    )

    val topVisitRadiosState: Flow<List<Station>> = stationsRepository.getTopVisitedStationsStream()

}

sealed interface StationsUiState {
    object Loading : StationsUiState

    data class Stations(
        val stations: List<FollowableStation>,
    ) : StationsUiState

    object Empty : StationsUiState
}


