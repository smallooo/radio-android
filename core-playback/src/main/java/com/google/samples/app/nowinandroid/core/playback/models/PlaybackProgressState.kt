/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.google.samples.app.nowinandroid.core.playback.models



data class PlaybackProgressState(
    val total: Long = 0L,
    val position: Long = 0L,
    val elapsed: Long = 0L,
    val buffered: Long = 0L,
) {

    val progress get() = ((position.toFloat() + elapsed) / (total + 1).toFloat()).coerceIn(0f, 1f)
    val bufferedProgress get() = ((buffered.toFloat()) / (total + 1).toFloat()).coerceIn(0f, 1f)

    val currentDuration get() = (position + elapsed).millisToDuration()
    val totalDuration get() = total.millisToDuration()


    fun timeAddZeros(number: Int?, ifZero: String = ""): String {
        return when (number) {
            0 -> ifZero
            in 1..9 -> "0$number"
            else -> number.toString()
        }
    }

    fun Long.millisToDuration(): String {
        val seconds = (this / 1000).toInt() % 60
        val minutes = (this / (1000 * 60) % 60).toInt()
        val hours = (this / (1000 * 60 * 60) % 24).toInt()
        "${timeAddZeros(hours)}:${timeAddZeros(minutes, "0")}:${timeAddZeros(seconds, "00")}".apply {
            return if (startsWith(":")) replaceFirst(":", "") else this
        }
    }
}


