package com.google.samples.apps.nowinandroid.feature.foryou


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountrySource @Inject constructor(private val countryApi: RadioListApi) {
    private var _countryList: ArrayList<Country>? = null

    suspend fun getCountryList() = withContext(Dispatchers.IO){
        if(_countryList == null){
            _countryList = countryApi.getCountryList()
        }
        return@withContext _countryList
    }

    private fun CountryListResponse.mapCountriesToItems(): Any {
        return this.countries
    }
}