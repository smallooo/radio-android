package com.google.samples.apps.nowinandroid.feature.foryou


class CountryCategoriesContract {

    data class State(
        val categories: List<Country> = listOf(),
        val isLoading: Boolean = false
    )

    sealed class Effect {
        object DataWasLoaded : Effect()
    }
}