package com.google.samples.apps.nowinandroid.core.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class StationsTag(
    val name: String = "",
    val stationcount: String = "",
) : Parcelable

