package com.google.samples.apps.nowinandroid.feature.foryou

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalStationsSource @Inject constructor(private val radioListApi: RadioListApi) {
    private var _localRadioList: ArrayList<Station>? = null

    suspend fun getLocalStationsList() = withContext(Dispatchers.IO){
        if(_localRadioList == null){
            _localRadioList = radioListApi.getListByCountry()
        }
        return@withContext _localRadioList
    }
}