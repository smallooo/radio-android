/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.dmhsh.samples.apps.nowinandroid.playback

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.dmhsh.samples.app.nowinandroid.core.playback.*
import com.dmhsh.samples.app.nowinandroid.core.playback.*
import com.dmhsh.samples.app.nowinandroid.core.playback.models.PlaybackProgressState
import com.dmhsh.samples.app.nowinandroid.core.playback.players.AudioPlayer
import com.dmhsh.samples.app.nowinandroid.core.playback.players.DatmusicPlayer
import com.dmhsh.samples.app.nowinandroid.core.playback.players.QUEUE_TITLE_KEY
import com.dmhsh.samples.apps.nowinandroid.core.database.model.StationEntity
import com.dmhsh.samples.apps.nowinandroid.core.model.data.Station
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.util.concurrent.TimeUnit

const val PLAYBACK_PROGRESS_INTERVAL = 1000L

interface PlaybackConnection {
    val isConnected: StateFlow<Boolean>
    val playbackState: StateFlow<PlaybackStateCompat>
    val nowPlaying: StateFlow<MediaMetadataCompat>
    val playingStation: StateFlow<Station>

    var mediaController: MediaControllerCompat?
    val transportControls: MediaControllerCompat.TransportControls?
    val playbackProgress: StateFlow<PlaybackProgressState>

    fun playAudio(station : Station)

    fun stopPlayInSeconds(seconds: Long)

    val timeRemained: Int
}

class PlaybackConnectionImpl(
    val context: Context,
    serviceComponent: ComponentName,
    private val audioPlayer: AudioPlayer,
    private val radioPlayer: DatmusicPlayer,
    coroutineScope: CoroutineScope = ProcessLifecycleOwner.get().lifecycleScope,
) : PlaybackConnection, CoroutineScope by coroutineScope {
    override val isConnected = MutableStateFlow(false)
    override val playbackState = MutableStateFlow(NONE_PLAYBACK_STATE)
    override val nowPlaying = MutableStateFlow(NONE_PLAYING)
    override val playingStation = MutableStateFlow(Station())
    override var mediaController: MediaControllerCompat? = null
    private var playbackProgressInterval: Job = Job()
    override val transportControls get() = mediaController?.transportControls
    override val playbackProgress  = MutableStateFlow(PlaybackProgressState())

    var localTimeRemained = 0

    init { startPlaybackProgress() }

    override fun playAudio(station : Station) {
        playingStation.value = station
        transportControls?.playFromUri(station.url_resolved.toUri(),
            Bundle().apply {
                //putStringArray(QUEUE_LIST_KEY, ["1","2"])
                putString(QUEUE_TITLE_KEY, "Audio".toString())
                putString("ARTIST", "ARTIST".toString())
                putString(QUEUE_TITLE_KEY, "Audio".toString())
            }
        )

        
        launch { radioPlayer.playRadio(station) }
    }

    override fun stopPlayInSeconds(seconds: Long) {
        localTimeRemained = (seconds / 60).toInt()
        radioPlayer.stopInSeconds(true, seconds)
    }

    override val timeRemained: Int
        get() = localTimeRemained

    private val mediaBrowserConnectionCallback = MediaBrowserConnectionCallback(context)

    private val mediaBrowser = MediaBrowserCompat(context, serviceComponent,
        mediaBrowserConnectionCallback, null).apply { connect() }

    private fun startPlaybackProgress() = launch {
        combine(playbackState, nowPlaying, ::Pair).collectLatest { (state, current) ->
            playbackProgressInterval.cancel()
            val duration = current.duration
            val position = state.position

            if (state == NONE_PLAYBACK_STATE || current == NONE_PLAYING || duration < 1)
                return@collectLatest

            val initial = PlaybackProgressState(duration, position, buffered = audioPlayer.bufferedPosition())
            playbackProgress.value = initial

            if (state.isPlaying && !state.isBuffering)
                starPlaybackProgressInterval(initial)
        }
    }

    private fun starPlaybackProgressInterval(initial: PlaybackProgressState) {
        playbackProgressInterval = launch {
            flowInterval(PLAYBACK_PROGRESS_INTERVAL).collectLatest { ticks ->
                val elapsed = PLAYBACK_PROGRESS_INTERVAL * (ticks + 1)
                playbackProgress.value = initial.copy(elapsed = elapsed, buffered = audioPlayer.bufferedPosition())
            }
        }
    }

    fun flowInterval(interval: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS): Flow<Int> {
        val delayMillis = timeUnit.toMillis(interval)
        return channelFlow {
            var tick = 0
            send(tick)
            while (true) {
                delay(delayMillis)
                send(++tick)
            }
        }
    }

    private inner class MediaBrowserConnectionCallback(private val context: Context) :
        MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            mediaController = MediaControllerCompat(context, mediaBrowser.sessionToken).apply {
                registerCallback(MediaControllerCallback())
            }

            isConnected.value = true
        }

        override fun onConnectionSuspended() {
            isConnected.value = false
        }

        override fun onConnectionFailed() {
            isConnected.value = false
        }
    }

    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            playbackState.value = state ?: return
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            nowPlaying.value = metadata ?: return
        }

        override fun onQueueChanged(queue: MutableList<MediaSessionCompat.QueueItem>?) {
            Timber.d("New queue: size=${queue?.size}")
        }

        override fun onRepeatModeChanged(repeatMode: Int) {

        }

        override fun onShuffleModeChanged(shuffleMode: Int) {

        }

        override fun onSessionDestroyed() {
            mediaBrowserConnectionCallback.onConnectionSuspended()
        }
    }

}
