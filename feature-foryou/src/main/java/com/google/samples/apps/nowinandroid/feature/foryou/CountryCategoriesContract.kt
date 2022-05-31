package com.google.samples.apps.nowinandroid.feature.foryou

import com.google.samples.apps.nowinandroid.core.model.data.Country


class CountryCategoriesContract {

    data class State(
        val categories: List<Country> = listOf(),
        val isLoading: Boolean = false
    )

    sealed class Effect {
        object DataWasLoaded : Effect()
    }
}