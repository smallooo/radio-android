package com.dmhsh.samples.apps.nowinandroid.core.data

import android.util.Log
import com.dmhsh.samples.apps.nowinandroid.core.database.dao.StationDao
import com.dmhsh.samples.apps.nowinandroid.core.model.data.Country
import com.dmhsh.samples.apps.nowinandroid.core.model.data.LanguageTag
import com.dmhsh.samples.apps.nowinandroid.core.model.data.Station
import com.dmhsh.samples.apps.nowinandroid.core.model.data.StationsTag
import com.dmhsh.samples.apps.nowinandroid.core.network.model.NetworkStation
import com.dmhsh.samples.apps.nowinandroid.core.network.retrofit.RadioListApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetSource @Inject constructor(private val stationDao: StationDao, private val radioListApi: RadioListApi) {
    private var _localRadioList: List<NetworkStation>? = null
    private var _searchRadioList: List<Station>? = null
    private var _topVisitRadioList: List<Station>? = null
    private var _topVotedRadioList: ArrayList<Station>? = null
    private var _lateUpdateRadioList: List<Station>? = null
    private var _lastClickRadioList: List<Station>? = null
    private var _stationsTagList: List<StationsTag>? = null
    private var _countryList: List<Country>? = null
    private var _languageList: List<LanguageTag>? = null
    private var _searchList: List<Station>? = null

    //Local Stations
    suspend fun getLocalList(type: String, param : String) = withContext(Dispatchers.IO){
        if(_localRadioList == null) _localRadioList = radioListApi.getListByCountry("bycountrycodeexact", Locale.getDefault().country)
        return@withContext _localRadioList
    }

    //Local Stations
    suspend fun getSearch(type: String, param : String) = withContext(Dispatchers.IO){
        _searchRadioList = radioListApi.searchByTypeList(type, param)
        return@withContext _searchRadioList
    }

    suspend fun getTopClickList() = withContext(Dispatchers.IO){
        if(_topVisitRadioList == null) {
            try {
                _topVisitRadioList = radioListApi.getTopClick()
            }catch (e : Exception){
            }
        }
        return@withContext _topVisitRadioList
    }

    suspend fun gettopVotedList() = withContext(Dispatchers.IO){
        try {
            if(_topVotedRadioList == null) _topVotedRadioList = radioListApi.getTopVote()
        } catch (e: Exception) {

        }
        return@withContext _topVotedRadioList
    }

    suspend fun getLateUpdateStationsList() = withContext(Dispatchers.IO){
        try {
            if(_lateUpdateRadioList == null) _lateUpdateRadioList = radioListApi.getLateUpdated()
        } catch (e: Exception) {
        }
        return@withContext _lateUpdateRadioList
    }

    suspend fun getLastClickList() = withContext(Dispatchers.IO){
        try {
            if(_lastClickRadioList == null) _lastClickRadioList = radioListApi.getLastClick()
        } catch (e: Exception) {
        }
        return@withContext _lastClickRadioList
    }

    suspend fun getTagList() = withContext(Dispatchers.IO){
        try {
            if(_stationsTagList == null) _stationsTagList = radioListApi.getTags()
        } catch (e: Exception) {
        }
        return@withContext _stationsTagList
    }

    suspend fun getCountryList() = withContext(Dispatchers.IO){
        try {
            if(_countryList == null) _countryList = radioListApi.getCountries()
        } catch (e: Exception) {
        }
        return@withContext _countryList
    }

    suspend fun getLanguageList() = withContext(Dispatchers.IO){
        try {
            if(_languageList == null) _languageList = radioListApi.getLanguages()
        } catch (e: Exception) {
        }
        return@withContext _languageList
    }

    suspend fun searchByTypeList(type: String, param: String) = withContext(Dispatchers.IO){
        try {
            _searchList = radioListApi.searchByTypeList(type, param.substring(1))
        } catch (e: Exception) {
        }
        return@withContext _searchList
    }
}