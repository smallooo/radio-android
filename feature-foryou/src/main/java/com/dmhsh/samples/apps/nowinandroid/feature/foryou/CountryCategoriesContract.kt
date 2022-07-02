package com.dmhsh.samples.apps.nowinandroid.feature.foryou

import com.dmhsh.samples.apps.nowinandroid.core.model.data.Country


class CountryCategoriesContract {

    data class State(
        val categories: List<Country> = listOf(),
        val isLoading: Boolean = false
    )

    sealed class Effect {
        object DataWasLoaded : com.dmhsh.samples.apps.nowinandroid.feature.foryou.CountryCategoriesContract.Effect()
    }
}