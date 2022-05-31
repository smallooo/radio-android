package com.google.samples.apps.nowinandroid.feature.foryou

import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RadioListApi @Inject constructor(private val service: Service) {
    suspend fun getCountryList(): ArrayList<Country> = service.getCountryList()
    suspend fun getListByCountry(type: String, param: String): ArrayList<Station> = service.getListByCountry(type, param)
    interface Service {
        @GET("json/countries")
        suspend fun getCountryList(): ArrayList<Country>

        @GET("/json/stations/{type}/{param}")
        suspend fun getListByCountry(@Path("type")type: String, @Path("param") param: String): ArrayList<Station>

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

    companion object {
        const val API_URL = "http://de1.api.radio-browser.info/"
    }
}


