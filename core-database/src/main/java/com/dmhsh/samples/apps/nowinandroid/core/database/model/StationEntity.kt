package com.dmhsh.samples.apps.nowinandroid.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dmhsh.samples.apps.nowinandroid.core.model.data.Station
import javax.annotation.Nullable



private val MULTIPLE_ARTIST_SPLIT_REGEX = Regex("((,)|(feat\\.)|(ft\\.))")
fun String.artists() = split(MULTIPLE_ARTIST_SPLIT_REGEX, 10).map { it.trim() }
fun String.mainArtist() = split(MULTIPLE_ARTIST_SPLIT_REGEX, 10).first().trim()

typealias StationId = String
typealias StationIds = List<StationId>
typealias Stations = List<StationEntity>


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
    @Nullable
    val lastchangetime_iso8601: String = "",
    val codec: String = "",
    val bitrate: String = "",
    val hls: String = "",
    @Nullable
    @ColumnInfo(defaultValue = "")
    val lastcheckok: String = "",
    @Nullable
    @ColumnInfo(defaultValue = "")
    val lastchecktime: String = "",
    @Nullable
    @ColumnInfo(defaultValue = "")
    val lastchecktime_iso8601: String = "",
    @Nullable
    @ColumnInfo(defaultValue = "")
    val lastcheckoktime: String = "",
    @Nullable
    @ColumnInfo(defaultValue = "")
    val lastcheckoktime_iso8601: String = "",
    @Nullable
    @ColumnInfo(defaultValue = "")
    val lastlocalchecktime: String = "",
    @Nullable
    @ColumnInfo(defaultValue = "")
    val lastlocalchecktime_iso8601: String = "",
    @Nullable
    @ColumnInfo(defaultValue = "")
    val clicktimestamp: String = "",
    @Nullable
    @ColumnInfo(defaultValue = "")
    val clicktimestamp_iso8601: String = "",
    @Nullable
    @ColumnInfo(defaultValue = "")
    val clickcount: String = "",
    @Nullable
    @ColumnInfo(defaultValue = "")
    val clicktrend: String = "",
    @Nullable
    @ColumnInfo(defaultValue = "")
    val ssl_error: String = "",
    @Nullable
    @ColumnInfo(defaultValue = "")
    val geo_lat: String = "",
    @Nullable
    @ColumnInfo(defaultValue = "")
    val geo_long: String = "",
    @Nullable
    @ColumnInfo(defaultValue = "")
    val has_extended_info: String = "",
    @Nullable
    @ColumnInfo(defaultValue = false.toString())
    val favorited: Boolean = false,
    @Nullable
    @ColumnInfo(defaultValue = "")
    val lastPlayedTime: String = ""
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
    has_extended_info = has_extended_info,
    favorited = favorited,
    lastPlayedTime = lastPlayedTime
)


fun Station.asExternalModel() = StationEntity(
    changeuuid = changeuuid,
    stationuuid = stationuuid,
    serveruuid = serveruuid?:"",
    name = name,
    url = url,
    url_resolved = url_resolved?:"",
    homepage = homepage?:"",
    favicon =favicon?:"",
    tags = tags?:"",
    country = country?:"",
    countrycode = countrycode?:"",
    iso_3166_2 = iso_3166_2?:"",
    state =state?:"",
    language = language?:"",
    languagecodes = languagecodes?:"",
    votes = votes?:"",
    lastchangetime = lastchangetime?:"",
    lastchangetime_iso8601 = lastchangetime_iso8601?:"",
    codec = codec?:"",
    bitrate = bitrate?:"",
    hls = hls?:"",
    lastcheckok = lastcheckok?:"",
    lastchecktime = lastchecktime?:"",
    lastchecktime_iso8601 = lastchecktime_iso8601?:"",
    lastcheckoktime = lastcheckoktime?:"",
    lastcheckoktime_iso8601 = lastcheckoktime_iso8601?:"",
    lastlocalchecktime = lastlocalchecktime?:"",
    lastlocalchecktime_iso8601 = lastlocalchecktime_iso8601?:"",
    clicktimestamp =clicktimestamp?:"",
    clicktimestamp_iso8601 = clicktimestamp_iso8601?:"",
    clickcount = clickcount?:"",
    clicktrend = clicktrend?:"",
    ssl_error = ssl_error,
    geo_lat = geo_lat?:"",
    geo_long = geo_long?:"",
    has_extended_info = has_extended_info?:"",
    favorited = favorited?:false,
    lastPlayedTime = lastPlayedTime?:""
)
