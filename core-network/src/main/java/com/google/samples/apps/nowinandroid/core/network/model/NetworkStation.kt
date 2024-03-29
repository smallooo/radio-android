package com.google.samples.apps.nowinandroid.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkStation(
    val changeuuid: String,
    val stationuuid: String,
    val serveruuid: String = "",
    val name: String,
    val url: String,
    val url_resolved: String,
    val homepage: String,
    val favicon: String,
    val tags: String,
    val country: String,
    val countrycode: String,
    val iso_3166_2: String,
    val state: String,
    val language: String,
    val languagecodes: String,
    val votes: String,
    val lastchangetime: String,
    val lastchangetime_iso8601: String,
    val codec: String,
    val bitrate: String,
    val hls: String,
    val lastcheckok: String,
    val lastchecktime: String,
    val lastchecktime_iso8601: String,
    val lastcheckoktime: String,
    val lastcheckoktime_iso8601: String,
    val lastlocalchecktime: String,
    val lastlocalchecktime_iso8601: String,
    val clicktimestamp: String,
    val clicktimestamp_iso8601: String,
    val clickcount: String,
    val clicktrend: String,
    val ssl_error: String,
    val geo_lat: String,
    val geo_long: String,
    val has_extended_info: String,

)