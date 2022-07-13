package com.dmhsh.samples.apps.nowinandroid.feature.foryou

import com.dmhsh.samples.apps.nowinandroid.core.model.data.Station


class LocalStationsContract {

    data class State(
        val localStations: List<Station> = listOf(),
        var initStatus: Boolean = true,
        var isLoading: Boolean = false

    )

    sealed class Effect {
        object DataWasLoaded : LocalStationsContract.Effect()
    }
}