/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.dmhsh.samples.apps.nowinandroid.core.ui.media.radioStations

import com.dmhsh.samples.apps.nowinandroid.core.model.data.Station
import com.dmhsh.samples.apps.nowinandroid.core.ui.R


sealed class AudioItemAction(open val station: Station) {
    data class Play(override val station: Station) : AudioItemAction(station)
    data class ExtraAction(val actionLabelRes: Int, override val station: Station) : AudioItemAction(station)


    fun handleExtraAction(extraActionLabelRes: Int, actionHandler: AudioActionHandler, onExtraAction: (ExtraAction) -> Unit) =
        handleExtraActions(actionHandler) {
            when (it.actionLabelRes) {
                extraActionLabelRes -> onExtraAction(it)
                else -> actionHandler(it)
            }
        }

    fun handleExtraActions(actionHandler: AudioActionHandler, onExtraAction: (ExtraAction) -> Unit) = when (this is ExtraAction) {
        true -> onExtraAction(this)
        else -> actionHandler(this)
    }

    companion object {
        fun from(actionLabelRes: Int, station: Station) = when (actionLabelRes) {
            R.string.back -> Play(station)
            else -> Play(station)

        }
    }
}
