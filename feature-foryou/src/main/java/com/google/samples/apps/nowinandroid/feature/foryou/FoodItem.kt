package com.google.samples.apps.nowinandroid.feature.foryou

data class FoodItem(
    val id: String,
    val name: String,
    val thumbnailUrl: String,
    val description: String = ""
)
