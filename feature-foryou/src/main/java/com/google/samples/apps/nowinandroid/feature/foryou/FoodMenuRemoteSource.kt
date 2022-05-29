package com.google.samples.apps.nowinandroid.feature.foryou


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodMenuRemoteSource @Inject constructor(private val countryApi: CountryListApi) {

    private var cachedCategories: List<FoodItem>? = null
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