package com.dmhsh.feature_history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmhsh.samples.apps.nowinandroid.core.data.repository.StationsRepository
import com.dmhsh.samples.apps.nowinandroid.core.model.data.FollowableStation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    stationsRepository: StationsRepository,
) : ViewModel() {

    val playHistoryStationsState: StateFlow<StationsUiState> = combine(
        stationsRepository.getPlayHistory(),
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
}



sealed interface StationsUiState {
    object Loading : StationsUiState

    data class Stations(
        val stations: List<FollowableStation>,
    ) : StationsUiState

    object Empty : StationsUiState
}



sealed interface AuthorUiState {
    //data class Success(val followableAuthor: FollowableAuthor) : AuthorUiState
    object Error : AuthorUiState
    object Loading : AuthorUiState
}

sealed interface NewsUiState {
    //data class Success(val news: List<NewsResource>) : NewsUiState
    object Error : NewsUiState
    object Loading : NewsUiState
}

data class AuthorScreenUiState(
    val authorState: AuthorUiState,
    val newsState: NewsUiState
)


