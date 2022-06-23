package com.google.samples.apps.nowinandroid.core.network.retrofit

import com.google.samples.apps.nowinandroid.core.model.data.Country
import com.google.samples.apps.nowinandroid.core.model.data.Station
import com.google.samples.apps.nowinandroid.core.network.model.NetworkStation
import retrofit2.http.GET

import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RadioListApi @Inject constructor(private val service: Service) {
    suspend fun getCountryList(): ArrayList<Country> = service.getCountryList()
    suspend fun getListByCountry(type: String, param: String): List<NetworkStation> = service.getListByCountry()
    interface Service {
        @GET("json/stations/lastchange/100")
        suspend fun getCountryList(): ArrayList<Country>

        @GET("/json/stations/{type}/{param}")
        suspend fun getListByCountryDemo(@Path("type")type: String, @Path("param") param: String): ArrayList<Station>

        @GET("/json/stations/bycountry/japan")
        suspend fun getListByCountry(): ArrayList<NetworkStation>

        @GET("/json/stations")
        suspend fun getStationsByLocation(): ArrayList<NetworkStation>

        @GET("/json/stations")
        suspend fun geStationsByCountry(): ArrayList<NetworkStation>

        @GET("/json/stations")
        suspend fun getStationsByVisit(): ArrayList<NetworkStation>

        @GET("/json/stations")
        suspend fun getStationByVote(): ArrayList<NetworkStation>

        @GET("/json/stations")
        suspend fun getStationsByUpdate(): ArrayList<NetworkStation>

        @GET("/json/stations")
        suspend fun getStationsByNowPlaying(): ArrayList<NetworkStation>

        @GET("/json/stations")
        suspend fun getStationsByLabel(): ArrayList<NetworkStation>

        /*
        @GET("/json/stations/bycountry/japan")
         本地电台            "/json/stations/bycountry/japan"
         访问排行            "json/stations/topclick/100";
         投票排行            "json/stations/topclick/100";
         最近更新            "json/stations/lastchange/100";
         正在播放            "json/stations/lastclick/100";
         标签
         国家
         语言
         搜索

        private String itsAdressWWWLocal = "json/stations/bycountryexact/internet?order=clickcount&reverse=true";
        private String itsAdressWWWTopClick = "json/stations/topclick/100";
        private String itsAdressWWWTopVote = "json/stations/topvote/100";
        private String itsAdressWWWChangedLately = "json/stations/lastchange/100";
        private String itsAdressWWWCurrentlyHeard = "json/stations/lastclick/100";
        private String itsAdressWWWTags = "json/tags";
        private String itsAdressWWWCountries = "json/countrycodes";
        private String itsAdressWWWLanguages = "json/languages";
         */
    }
    //http://89.58.16.19/json/station/
    companion object { const val API_URL = "http://at1.api.radio-browser.info/" }
}


