/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.google.samples.apps.nowinandroid.playback

import android.graphics.Bitmap
import android.media.session.PlaybackState
import android.net.Uri


val NONE_PLAYBACK_STATE: PlaybackState = PlaybackState.Builder()
    .setState(PlaybackState.STATE_NONE, 0, 0f)
    .build()
