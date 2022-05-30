package com.google.samples.apps.nowinandroid.feature.foryou

import retrofit2.http.GET
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountryListApi @Inject constructor(private val service: Service) {
    suspend fun getCountryList(): ArrayList<Country> = service.getCountryList()
    interface Service {
        @GET("json/countries")
        suspend fun getCountryList(): ArrayList<Country>


        @GET("/json/stations/bycountry/japan")
        suspend fun getListByCountry(): ArrayList<Station>
    }

    companion object {
        const val API_URL = "http://de1.api.radio-browser.info/"
    }
}


