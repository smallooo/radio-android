package com.google.samples.apps.nowinandroid.feature.foryou


import dagger.Provides
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountryListApi @Inject constructor(private val service: Service) {

    suspend fun getCountryList(): ArrayList<Country> = service.getCountryList()



    interface Service {
        @GET("json/countries")
        suspend fun getCountryList(): ArrayList<Country>

    }

    companion object {
        const val API_URL = "http://de1.api.radio-browser.info/"
    }
}


