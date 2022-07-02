package com.dmhsh.samples.apps.nowinandroid.core.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class LanguageTag(
    val name: String = "",
    val iso_639: String = "",
    val stationcount: String = "",
) : Parcelable

