/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.google.samples.apps.nowinandroid.common.compose

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.staticCompositionLocalOf
import com.google.samples.apps.nowinandroid.playback.PlaybackConnection

val LocalScaffoldState = staticCompositionLocalOf<ScaffoldState> { error("No LocalScaffoldState provided") }

//val LocalAnalytics = staticCompositionLocalOf<FirebaseAnalytics> {
//    error("No LocalAnalytics provided")
//}
//
val LocalPlaybackConnection = staticCompositionLocalOf<PlaybackConnection> {
    error("No LocalPlaybackConnection provided")
}
