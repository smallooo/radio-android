/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.google.samples.apps.nowinandroid.playback


import android.support.v4.media.session.MediaControllerCompat
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.samples.apps.nowinandroid.core.model.data.Station
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


interface PlaybackConnection {
    val isConnected: StateFlow<Boolean>
//    val playbackState: StateFlow<PlaybackStateCompat>
    var mediaController: MediaControllerCompat?
    val transportControls: MediaControllerCompat.TransportControls?
    fun playAudio(station: Station)
}


class PlaybackConnectionImpl(
    coroutineScope: CoroutineScope = ProcessLifecycleOwner.get().lifecycleScope,
) : PlaybackConnection, CoroutineScope by coroutineScope {
    override val isConnected = MutableStateFlow(false)
//    override val playbackState = MutableStateFlow(NONE_PLAYBACK_STATE)
//
        override var mediaController: MediaControllerCompat? = null
        override val transportControls get() = mediaController?.transportControls
//
    override fun playAudio(station: Station) {
//        transportControls?.playFromMediaId(
//            MediaId(MEDIA_TYPE_AUDIO, station.url_resolved).toString(),
//            Bundle().apply {
//                putString(QUEUE_TITLE_KEY, "play audio")
//            }
//        )
    }
}
