package com.google.samples.apps.nowinandroid.core.data

import android.util.Log
import com.google.samples.apps.nowinandroid.core.database.dao.StationDao
import com.google.samples.apps.nowinandroid.core.model.data.Station
import com.google.samples.apps.nowinandroid.core.network.model.NetworkStation
import com.google.samples.apps.nowinandroid.core.network.retrofit.RadioListApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalStationsSource @Inject constructor(private val stationDao: StationDao, private val radioListApi: RadioListApi) {
    private var _localRadioList: List<NetworkStation>? = null
    private var _topVisitRadioList: List<Station>? = null

    suspend fun getLocalStationsList(type: String, param : String) = withContext(Dispatchers.IO){
        if(_localRadioList == null){ _localRadioList = radioListApi.getListByCountry(type, param) }
        return@withContext _localRadioList
    }

    suspend fun getTopClickStationsList() = withContext(Dispatchers.IO){
        if(_topVisitRadioList == null){ _topVisitRadioList = radioListApi.getTopClick() }
        return@withContext _topVisitRadioList
    }
}