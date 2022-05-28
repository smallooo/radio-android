package com.google.samples.apps.nowinandroid.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.samples.apps.nowinandroid.core.model.data.Country


@Entity(
    tableName = "country",
)
data class CountryEntity (@PrimaryKey
                          val id: String,
                          val name: String,
                          val iso_3166_1: String,
                          @ColumnInfo(defaultValue = "")
                          val stationcount: String,
)

fun CountryEntity.asExternalModel() = Country(
    id = id,
    name = name,
    iso_3166_1 = iso_3166_1,
    stationcount = stationcount
)