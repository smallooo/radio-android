package com.google.samples.apps.nowinandroid.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.samples.apps.nowinandroid.core.model.data.Station
import javax.annotation.Nullable

@Entity(tableName = "stations",)
data class StationEntity(

    val changeuuid: String,
    @PrimaryKey
    val stationuuid: String,
    @Nullable
    val serveruuid: String,
    val name: String = "",
    val url: String = "",
    val url_resolved: String = "",
    val homepage: String = "",
    val favicon: String = "",
    val tags: String = "",
    val country: String = "",
    val countrycode: String = "",
    val iso_3166_2: String = "",
    val state: String = "",
    val language: String = "",
    val languagecodes: String = "",
    val votes: String = "",
    val lastchangetime: String = "",
    val lastchangetime_iso8601: String = "",
    val codec: String = "",
    val bitrate: String = "",
    val hls: String = "",
    val lastcheckok: String = "",
    val lastchecktime: String = "",
    val lastchecktime_iso8601: String = "",
    val lastcheckoktime: String = "",
    val lastcheckoktime_iso8601: String = "",
    val lastlocalchecktime: String = "",
    val lastlocalchecktime_iso8601: String = "",
    val clicktimestamp: String = "",
    val clicktimestamp_iso8601: String = "",
    val clickcount: String = "",
    val clicktrend: String = "",
    val ssl_error: String = "",
    val geo_lat: String = "",
    val geo_long: String = "",
    val has_extended_info: String = "",

    )


fun StationEntity.asExternalModel() = Station(
    changeuuid = changeuuid,
    stationuuid = stationuuid,
    serveruuid = serveruuid,
    name = name,
    url = url,
    url_resolved = url_resolved,
    homepage = homepage,
    favicon =favicon,
    tags = tags,
    country = country,
    countrycode = countrycode,
    iso_3166_2 = iso_3166_2,
    state =state,
    language = language,
    languagecodes = languagecodes,
    votes = votes,
    lastchangetime = lastchangetime,
    lastchangetime_iso8601 = lastchangetime_iso8601,
    codec = codec,
    bitrate = bitrate,
    hls = hls,
    lastcheckok = lastcheckok,
    lastchecktime = lastchecktime,
    lastchecktime_iso8601 = lastchecktime_iso8601,
    lastcheckoktime = lastcheckoktime,
    lastcheckoktime_iso8601 = lastcheckoktime_iso8601,
    lastlocalchecktime = lastlocalchecktime,
    lastlocalchecktime_iso8601 = lastlocalchecktime_iso8601,
    clicktimestamp =clicktimestamp,
    clicktimestamp_iso8601 = clicktimestamp_iso8601,
    clickcount = clickcount,
    clicktrend = clicktrend,
    ssl_error = ssl_error,
    geo_lat = geo_lat,
    geo_long = geo_long,
    has_extended_info = has_extended_info
)
