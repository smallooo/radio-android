package com.google.samples.apps.nowinandroid.core.data

import com.google.samples.apps.nowinandroid.core.model.data.Station
import com.google.samples.apps.nowinandroid.core.network.retrofit.RadioListApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalStationsSource @Inject constructor(private val radioListApi: RadioListApi) {
    private var _localRadioList: ArrayList<Station>? = null

    suspend fun getLocalStationsList(type: String, param : String) = withContext(Dispatchers.IO){
        if(_localRadioList == null){
            _localRadioList = radioListApi.getListByCountry(type, param) as ArrayList<Station>
        }
        return@withContext _localRadioList
    }
}