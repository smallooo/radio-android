package com.google.samples.apps.nowinandroid.feature.foryou


class LocalStationsContract {

    data class State(
        val localStations: List<Station> = listOf(),
        val isLoading: Boolean = false
    )

    sealed class Effect {
        object DataWasLoaded : Effect()
    }
}