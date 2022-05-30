package com.google.samples.apps.nowinandroid.feature.foryou

import com.google.gson.annotations.SerializedName

data class StationsListResponse(val countries: Any)

data class Station(
    @SerializedName("name") val name: String,
    @SerializedName("iso_3166_1") val iso_3166_1: String,
    @SerializedName("stationcount") val stationcount: String,
)
