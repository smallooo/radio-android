/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.dmhsh.samples.apps.nowinandroid.core.ui.media.radioStations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import com.dmhsh.samples.apps.nowinandroid.common.compose.LocalPlaybackConnection

import com.dmhsh.samples.apps.nowinandroid.playback.PlaybackConnection
import kotlinx.coroutines.launch


val LocalAudioActionHandler = staticCompositionLocalOf<AudioActionHandler> {
    error("No LocalAudioActionHandler provided")
}

typealias AudioActionHandler = (AudioItemAction) -> Unit

@Composable
fun audioActionHandler(
    playbackConnection: PlaybackConnection = LocalPlaybackConnection.current,
    clipboardManager: ClipboardManager = LocalClipboardManager.current,
): AudioActionHandler {
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()

    return { action ->
        when (action) {
            is AudioItemAction.Play -> playbackConnection.playAudio(action.station)
        }
    }
}
