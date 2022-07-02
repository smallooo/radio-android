package com.dmhsh.samples.apps.nowinandroid.core.data



//import com.dmhsh.samples.apps.nowinandroid.core.model.data.CountryListResponse
//import com.dmhsh.samples.apps.nowinandroid.core.model.data.Station
//
//import com.dmhsh.samples.apps.nowinandroid.core.network.retrofit.RadioListApi
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import javax.inject.Inject
//import javax.inject.Singleton
//
//@Singleton
//class TopClickStationsSource @Inject constructor(private val radioListApi: RadioListApi) {
//    private var _topVisitList: List<Station>? = null
//
//    suspend fun getTopClickList() = withContext(Dispatchers.IO){
//        if(_topVisitList == null){
//            _topVisitList = radioListApi.getTopClick()
//        }
//        return@withContext _topVisitList
//    }
//
//    private fun CountryListResponse.mapCountriesToItems(): Any {
//        return this.countries
//    }
//}