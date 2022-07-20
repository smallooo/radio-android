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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class  SearchListViewModel @Inject constructor(
    private val remoteSource: NetSource,
    private val stationsRepo: StationsRepo,
    private val stationDao: StationDao,
    ) : ViewModel() {

    var state by mutableStateOf(
        LocalStationsContract.State(
            localStations = listOf(),
            initStatus = true,
            isLoading = true
        )
    )

//    val searchRadiosState: StateFlow<StationsUiState> = combine(
//        stationsRepo.getAllStream(),
//    ) { availableStations ->
//        StationsUiState.Stations(stations = availableStations.get(0) )
//    }.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5_000),
//        initialValue = StationsUiState.Empty
//    )



    var effects = Channel<CountryCategoriesContract.Effect>(Channel.UNLIMITED)
        private set

    init {
        viewModelScope.launch {
           // getSearchStationList(type, param)
        }
    }

    fun getTagearch(type: String, param: String) {
        viewModelScope.launch {
            state = state.copy(localStations = emptyList(), initStatus = false, isLoading = true)
            effects.send(CountryCategoriesContract.Effect.DataWasLoaded)
            getSearchStationList(type, "#$param")
        }

    }

    suspend fun getSearchStationList(type: String, param: String) {
        val categories = remoteSource.searchByTypeList(type, param)
        if (categories != null) {
            val stations = ArrayList<StationEntity>()
            for(item in categories){ stations.add(item.asExternalModel()) }
            stationDao.upsertStations(entities = stations)
        }

        viewModelScope.launch {
            if(categories == null) {
                state = state.copy(localStations = emptyList(), initStatus = false, isLoading = false)
            }else{
                state = categories.let { state.copy(localStations = it, initStatus = false, isLoading = false) }!!
            }
            Log.e("aaa categories", categories?.size.toString()?:"")
            effects.send(CountryCategoriesContract.Effect.DataWasLoaded)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun upDateSearch(type: String, param: String){
        GlobalScope.launch(Dispatchers.IO) {
            state =  state.copy(localStations = emptyList(), initStatus = false, isLoading = true)
            getSearchStationList(type, param)
        }
    }

    fun setFavoritedStation(station: Station) = stationsRepo.setFavorite(station)

    fun setPlayHistory(station: Station) = stationsRepo.setPlayHistory(station)
}




