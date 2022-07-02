package com.dmhsh.samples.apps.nowinandroid.core.network.model

import kotlinx.serialization.Serializable


@Serializable
data class  NetworkCountry(
    val id: String,
    val name: String,
    val iso_3166_1: String,
    val stationcount: String,
)