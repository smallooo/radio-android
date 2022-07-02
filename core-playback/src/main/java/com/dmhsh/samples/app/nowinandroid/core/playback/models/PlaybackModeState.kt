/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.dmhsh.samples.app.nowinandroid.core.playback.models

import android.support.v4.media.session.PlaybackStateCompat

data class PlaybackModeState(
    val shuffleMode: Int = PlaybackStateCompat.SHUFFLE_MODE_INVALID,
    val repeatMode: Int = PlaybackStateCompat.REPEAT_MODE_INVALID,
)
