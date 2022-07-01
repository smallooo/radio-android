package com.google.samples.apps.nowinandroid.core.network.retrofit

import com.google.samples.apps.nowinandroid.core.model.data.Country
import com.google.samples.apps.nowinandroid.core.model.data.LanguageTag
import com.google.samples.apps.nowinandroid.core.model.data.Station
import com.google.samples.apps.nowinandroid.core.model.data.StationsTag
import com.google.samples.apps.nowinandroid.core.network.model.NetworkStation
import retrofit2.http.GET

import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RadioListApi @Inject constructor(private val service: Service) {
    suspend fun getCountryList(): ArrayList<Country> = service.getCountryList()
    suspend fun getListByCountry(type: String, param: String): List<NetworkStation> = service.getListByCountry()
    suspend fun getTopClick(): List<Station> = service.getTopClick()
    suspend fun getTopVote(): List<Station> = service.getTopVote()
    suspend fun getLateUpdated(): List<Station> = service.getRecentlyChanged()
    suspend fun getLastClick(): List<Station> = service.getLastClick()
    suspend fun getTags(): List<StationsTag> = service.getTags()
    suspend fun getCountries(): List<Country> = service.getCountries()
    suspend fun getLanguages(): List<LanguageTag> = service.getLanguages()
    suspend fun getStationsByConditionList(type: String, param: String): List<Station> = service.getStationsByConditionList(type, param)
    interface Service {
        @GET("json/countries")
        suspend fun getCountryList(): ArrayList<Country>

        @GET("/json/stations/{type}/{param}")
        suspend fun getListByCountryDemo(@Path("type")type: String, @Path("param") param: String): ArrayList<Station>

        @GET("/json/stations/bycountry/japan")
        suspend fun getListByCountry(): ArrayList<NetworkStation>

        @GET("/json/stations/topclick/200")
        suspend fun getTopClick(): ArrayList<Station>

        @GET("/json/stations/topvote/200")
        suspend fun getTopVote(): ArrayList<Station>

        @GET("/json/stations/lastchange/200")
        suspend fun getRecentlyChanged(): ArrayList<Station>

        @GET("/json/stations/lastclick/200")
        suspend fun getLastClick(): ArrayList<Station>

        @GET("/json/tags")
        suspend fun getTags(): ArrayList<StationsTag>

        @GET("/json/countrycodes")
        suspend fun getCountries(): ArrayList<Country>

        @GET("/json/languages")
        suspend fun getLanguages(): ArrayList<LanguageTag>

        @GET("/json/languages")
        suspend fun getSearchResult(): ArrayList<Station>

        @GET("/json/stations/{type}/{param}")
        suspend fun getStationsByConditionList(@Path("type")type: String, @Path("param") param: String): ArrayList<Station>
    }

    companion object { const val API_URL = "https://at1.api.radio-browser.info/" }
}


