package com.google.samples.apps.nowinandroid.core.model.data



data class CountryListResponse(val countries: Any)

data class Country(
    val name: String,
    val iso_3166_1: String,
     val stationcount: String,
)
