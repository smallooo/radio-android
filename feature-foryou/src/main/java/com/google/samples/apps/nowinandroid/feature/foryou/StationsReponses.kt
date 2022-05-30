package com.google.samples.apps.nowinandroid.feature.foryou

import com.google.gson.annotations.SerializedName

data class StationsListResponse(val countries: Any)

data class Station(
    @SerializedName("changeuuid") val changeuuid: String,
    @SerializedName("stationuuid") val stationuuid: String,
    @SerializedName("serveruuid") val serveruuid: String,
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String,
    @SerializedName("url_resolved") val url_resolved: String,
    @SerializedName("homepage") val homepage: String,
    @SerializedName("favicon") val favicon: String,
    @SerializedName("tags") val tags: String,
    @SerializedName("country") val country: String,
    @SerializedName("countrycode") val countrycode: String,
    @SerializedName("iso_3166_2") val iso_3166_2: String,
    @SerializedName("state") val state: String,
    @SerializedName("language") val language: String,
    @SerializedName("languagecodes") val languagecodes: String,
    @SerializedName("votes") val votes: String,
    @SerializedName("lastchangetime") val lastchangetime: String,
    @SerializedName("lastchangetime_iso8601") val lastchangetime_iso8601: String,
    @SerializedName("codec") val codec: String,
    @SerializedName("bitrate") val bitrate: String,
    @SerializedName("hls") val hls: String,
    @SerializedName("lastcheckok") val lastcheckok: String,
    @SerializedName("lastchecktime") val lastchecktime: String,
    @SerializedName("lastchecktime_iso8601") val lastchecktime_iso8601: String,
    @SerializedName("lastcheckoktime") val lastcheckoktime: String,
    @SerializedName("lastcheckoktime_iso8601") val lastcheckoktime_iso8601: String,
    @SerializedName("lastlocalchecktime") val lastlocalchecktime: String,
    @SerializedName("lastlocalchecktime_iso8601") val lastlocalchecktime_iso8601: String,
    @SerializedName("clicktimestamp") val clicktimestamp: String,
    @SerializedName("clicktimestamp_iso8601") val clicktimestamp_iso8601: String,
    @SerializedName("clickcount") val clickcount: String,
    @SerializedName("clicktrend") val clicktrend: String,
    @SerializedName("ssl_error") val ssl_error: String,
    @SerializedName("geo_lat") val geo_lat: String,
    @SerializedName("geo_long") val geo_long: String,
    @SerializedName("has_extended_info") val has_extended_info: String,
)
