package com.dmhsh.samples.apps.nowinandroid.feature.foryou

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmhsh.samples.apps.nowinandroid.core.data.NetSource
import com.dmhsh.samples.apps.nowinandroid.core.data.repository.StationsRepo
import com.dmhsh.samples.apps.nowinandroid.core.database.dao.StationDao
import com.dmhsh.samples.apps.nowinandroid.core.database.model.StationEntity
import com.dmhsh.samples.apps.nowinandroid.core.database.model.asExternalModel
import com.dmhsh.samples.apps.nowinandroid.core.model.data.Station

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopVoteViewModel @Inject constructor(private val remoteSource: NetSource,
                                           private val stationsRepo: StationsRepo,
                                           private val stationDao: StationDao,) : ViewModel() {


    var topVoteState: StateFlow<StationsUiState1> =
        stationsRepo.gettopVotedStream.map {
                availableStations ->
            Log.e("aaav", availableStations.size.toString())
            val stations = ArrayList<StationEntity>()
            for(item in availableStations){
                stations.add(item.asExternalModel())
            }
            stationDao.upsertStations(stations)
            StationsUiState1.Stations(stations1 = availableStations as ArrayList<Station>)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = StationsUiState1.Loading
    )


//    var state by mutableStateOf(
//        LocalStationsContract.State(
//             localStations = listOf(),
//            isLoading = true
//        )
//    )
//
//    var effects = Channel<LocalStationsContract.Effect>(UNLIMITED)
//        private set
//
//    init {
//        viewModelScope.launch {
//            getTopClickStationList()
//        }
//    }
//
//    private suspend fun getTopClickStationList() {
//        val categories = remoteSource.gettopVotedList()
//        viewModelScope.launch {
//            state = categories?.let { state.copy(localStations = it, isLoading = false) }!!
//            effects.send(LocalStationsContract.Effect.DataWasLoaded)
//        }
//    }



    fun setFavoritedStation(station: Station) = stationsRepo.setFavorite(station)

    fun setPlayHistory(station: Station) = stationsRepo.setPlayHistory(station)
}


sealed interface StationsUiState1 {
    object Loading : StationsUiState1

    data class Stations(
        val stations1: ArrayList<Station>,
    ) : StationsUiState1 {

    }

    object Empty : StationsUiState1
}



