package com.google.samples.apps.nowinandroid.feature.foryou

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.samples.apps.nowinandroid.core.data.LocalStationsSource
import com.google.samples.apps.nowinandroid.core.data.repository.StationsRepository
import com.google.samples.apps.nowinandroid.core.database.dao.StationDao
import com.google.samples.apps.nowinandroid.core.database.model.StationEntity
import com.google.samples.apps.nowinandroid.core.database.model.asExternalModel
import com.google.samples.apps.nowinandroid.core.model.data.Station

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class  SearchListViewModel @Inject constructor(
    private val remoteSource: LocalStationsSource,
    private val stationsRepository: StationsRepository,
    private val stationDao: StationDao,
    ) : ViewModel() {

    var type: String = "bytag"
    var param: String = "1012"

    var state by mutableStateOf(
        LocalStationsContract.State(
            localStations = listOf(),
            isLoading = true
        )
    )

    var effects = Channel<CountryCategoriesContract.Effect>(Channel.UNLIMITED)
        private set

    init {
        viewModelScope.launch {
            getSearchStationList(type, param)
        }
    }

    private suspend fun getSearchStationList(type: String, param: String) {
        val categories = remoteSource.getStationsByConditionList(type, param)
        if (categories != null) {
            val stations = ArrayList<StationEntity>()
            for(item in categories){ stations.add(item.asExternalModel()) }
            stationDao.upsertStations(entities = stations)
        }

        viewModelScope.launch {
            state = categories?.let { state.copy(localStations = it, isLoading = false) }!!
            effects.send(CountryCategoriesContract.Effect.DataWasLoaded)
        }
    }

    fun upDateSearch(type: String, param: String){
        GlobalScope.launch(Dispatchers.IO) {
            state =  state.copy(localStations = emptyList(), isLoading = true)
            getSearchStationList(type, param)
        }
    }

    fun setFavoritedStation(station: Station) = stationsRepository.setFavoriteStation(station)

    fun setPlayHistory(station: Station) = stationsRepository.setPlayHistory(station)
}




