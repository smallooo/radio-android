package com.google.samples.apps.nowinandroid.core.data.model

import com.google.samples.apps.nowinandroid.core.database.model.StationEntity
import com.google.samples.apps.nowinandroid.core.network.model.NetworkStation


fun NetworkStation.asEntity() = StationEntity(
    changeuuid = changeuuid,
    stationuuid = stationuuid,
    serveruuid = serveruuid?:"",
    name = name,
    url = url,
    url_resolved = url_resolved,
    homepage = homepage,
    favicon =favicon,
    tags = tags,
    country = country,
    countrycode = countrycode,
    iso_3166_2 = iso_3166_2?:"",
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
    lastlocalchecktime_iso8601 = lastlocalchecktime_iso8601?:"",
    clicktimestamp =clicktimestamp,
    clicktimestamp_iso8601 = clicktimestamp_iso8601?:"",
    clickcount = clickcount,
    clicktrend = clicktrend,
    ssl_error = ssl_error,
    geo_lat = geo_lat?:"",
    geo_long = geo_long?:"",
    has_extended_info = has_extended_info,
    favorited = false
)