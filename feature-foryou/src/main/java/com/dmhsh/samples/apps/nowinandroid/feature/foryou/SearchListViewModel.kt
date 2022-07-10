package com.dmhsh.samples.apps.nowinandroid.feature.foryou

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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class  SearchListViewModel @Inject constructor(
    private val remoteSource: NetSource,
    private val stationsRepo: StationsRepo,
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

    var effects = Channel<com.dmhsh.samples.apps.nowinandroid.feature.foryou.CountryCategoriesContract.Effect>(Channel.UNLIMITED)
        private set

    init {
        viewModelScope.launch {
           // getSearchStationList(type, param)
        }
    }

    private suspend fun getSearchStationList(type: String, param: String) {
        val categories = remoteSource.searchByTypeList(type, param)
        if (categories != null) {
            val stations = ArrayList<StationEntity>()
            for(item in categories){ stations.add(item.asExternalModel()) }
            stationDao.upsertStations(entities = stations)
        }

        viewModelScope.launch {
            state = categories?.let { state.copy(localStations = it, isLoading = false) }!!
            effects.send(com.dmhsh.samples.apps.nowinandroid.feature.foryou.CountryCategoriesContract.Effect.DataWasLoaded)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun upDateSearch(type: String, param: String){
        GlobalScope.launch(Dispatchers.IO) {
            state =  state.copy(localStations = emptyList(), isLoading = true)
            getSearchStationList(type, param)
        }
    }

    fun setFavoritedStation(station: Station) = stationsRepo.setFavorite(station)

    fun setPlayHistory(station: Station) = stationsRepo.setPlayHistory(station)
}




