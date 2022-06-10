/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.google.samples.apps.nowinandroid.core.ui.media.radioStations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import com.google.samples.apps.nowinandroid.core.compose.LocalPlaybackConnection
import com.google.samples.apps.nowinandroid.playback.PlaybackConnection
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
            is AudioItemAction.Play -> playbackConnection.playAudio()
        }
    }
}
