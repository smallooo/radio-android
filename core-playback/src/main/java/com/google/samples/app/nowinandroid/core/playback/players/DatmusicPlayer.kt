package com.google.samples.app.nowinandroid.core.playback.players

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import com.google.samples.app.nowinandroid.core.playback.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


typealias OnPrepared<T> = T.() -> Unit
typealias OnError<T> = T.(error: Throwable) -> Unit
typealias OnCompletion<T> = T.() -> Unit
typealias OnBuffering<T> = T.() -> Unit
typealias OnReady<T> = T.() -> Unit
typealias OnMetaDataChanged = DatmusicPlayer.() -> Unit
typealias OnIsPlaying<T> = T.(playing: Boolean, byUi: Boolean) -> Unit

const val REPEAT_MODE = "repeat_mode"
const val SHUFFLE_MODE = "shuffle_mode"
const val QUEUE_CURRENT_INDEX = "queue_current_index"
const val QUEUE_HAS_PREVIOUS = "queue_has_previous"
const val QUEUE_HAS_NEXT = "queue_has_next"

const val DEFAULT_FORWARD_REWIND = 10 * 1000

interface DatmusicPlayer {
    fun getSession(): MediaSessionCompat
    suspend fun playAudio(id: String, index: Int? = null)
    fun pause(extras: Bundle = bundleOf(BY_UI_KEY to true))
    suspend fun nextAudio(): String?
    fun stop(byUser: Boolean)
    fun release()
    fun onPrepared(prepared: OnPrepared<DatmusicPlayer>)
    fun onError(error: OnError<DatmusicPlayer>)
    fun onCompletion(completion: OnCompletion<DatmusicPlayer>)
    fun onMetaDataChanged(metaDataChanged: OnMetaDataChanged)
    fun updateData(list: List<String> = emptyList(), title: String? = null)
    fun setData(list: List<String> = emptyList(), title: String? = null)
    fun clearRandomAudioPlayed()

    fun updatePlaybackState(applier: PlaybackStateCompat.Builder.() -> Unit = {})
    fun setPlaybackState(state: PlaybackStateCompat)


}

@Singleton
class DatmusicPlayerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val audioPlayer: AudioPlayerImpl,
    private val queueManager: AudioQueueManagerImpl,
    private val audioFocusHelper: AudioFocusHelperImpl,
    ) : DatmusicPlayer, CoroutineScope by MainScope() {

    companion object {
        private const val queueStateKey = "player_queue_state"
    }

    private var isInitialized: Boolean = false

    private var isPlayingCallback: OnIsPlaying<DatmusicPlayer> = { _, _ -> }
    private var preparedCallback: OnPrepared<DatmusicPlayer> = {}
    private var errorCallback: OnError<DatmusicPlayer> = {}
    private var completionCallback: OnCompletion<DatmusicPlayer> = {}
    private var metaDataChangedCallback: OnMetaDataChanged = {}

    private val metadataBuilder = MediaMetadataCompat.Builder()
    private val stateBuilder = createDefaultPlaybackState()

    private val pendingIntent = PendingIntent.getBroadcast(context, 0, Intent(Intent.ACTION_MEDIA_BUTTON),
        PendingIntent.FLAG_IMMUTABLE
    )



    private val mediaSession = MediaSessionCompat(context, "App name", null, pendingIntent).apply {
        setCallback(
            MediaSessionCallback(this, this@DatmusicPlayerImpl, audioFocusHelper)
        )
        setPlaybackState(stateBuilder.build())

        val sessionIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        val sessionActivityPendingIntent = PendingIntent.getActivity(context, 0, sessionIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        setSessionActivity(sessionActivityPendingIntent)
        isActive = true
    }

    init {
        queueManager.setMediaSession(mediaSession)
        audioPlayer.onPrepared {
            preparedCallback(this@DatmusicPlayerImpl)
            launch {
                if (!mediaSession.isPlaying()) audioPlayer.seekTo(mediaSession.position())
                playAudio("",0)
            }
        }

        audioPlayer.onCompletion {
            completionCallback(this@DatmusicPlayerImpl)
            val controller = getSession().controller
            when (controller.repeatMode) {
                PlaybackStateCompat.REPEAT_MODE_ONE -> controller.transportControls.sendCustomAction(REPEAT_ONE, null)
                PlaybackStateCompat.REPEAT_MODE_ALL -> controller.transportControls.sendCustomAction(REPEAT_ALL, null)
                else -> launch { if (nextAudio() == null) goToStart() }
            }
        }
        audioPlayer.onBuffering {
            updatePlaybackState {
                setState(PlaybackStateCompat.STATE_BUFFERING, mediaSession.position(), 1F)
            }
        }
        audioPlayer.onIsPlaying { playing, byUi ->
            if (playing)
                updatePlaybackState {
                    setState(PlaybackStateCompat.STATE_PLAYING, mediaSession.position(), 1F)
                    setExtras(
                        bundleOf(
                            REPEAT_MODE to getSession().repeatMode,
                            SHUFFLE_MODE to getSession().shuffleMode
                        )
                    )
                }
            isPlayingCallback(playing, byUi)
        }
        audioPlayer.onReady {
            if (!audioPlayer.isPlaying()) {
                Timber.d("Player ready but not currently playing, requesting to play")
                audioPlayer.play()
            }
            updatePlaybackState {
                setState(PlaybackStateCompat.STATE_PLAYING, mediaSession.position(), 1F)
            }
        }
        audioPlayer.onError { throwable ->
            Timber.e(throwable, "AudioPlayer error")
            errorCallback(this@DatmusicPlayerImpl, throwable)
            isInitialized = false
            updatePlaybackState {
                setState(PlaybackStateCompat.STATE_ERROR, 0, 1F)
            }
        }
    }


    override fun getSession(): MediaSessionCompat = mediaSession

    override suspend fun playAudio(id: String, index: Int?) {
        if (isInitialized) {
            audioPlayer.play()
            return
        }

        val uri = "".toUri()
        val isSourceSet = true
        if (uri != null) audioPlayer.setSource(uri, false)


        if (isSourceSet) {
            isInitialized = true
            audioPlayer.prepare()
        } else {
           // Timber.e("Couldn't set new source")
        }
    }

    override fun pause(extras: Bundle) {
        if (isInitialized && (audioPlayer.isPlaying() || audioPlayer.isBuffering())) {
            audioPlayer.pause()
            updatePlaybackState {
                setState(PlaybackStateCompat.STATE_PAUSED, mediaSession.position(), 1F)
//                setExtras(
//                    extras + bundleOf(
//                        REPEAT_MODE to getSession().repeatMode,
//                        SHUFFLE_MODE to getSession().shuffleMode
//                    )
//                )
            }
        } else {
            //Timber.d("Couldn't pause player: ${audioPlayer.isPlaying()}, $isInitialized")
        }
    }

    override suspend fun nextAudio(): String? {
        return ""
    }

    override fun stop(byUser: Boolean) {

    }

    override fun release() {

    }

    override fun onPrepared(prepared: OnPrepared<DatmusicPlayer>) {

    }

    override fun onError(error: OnError<DatmusicPlayer>) {

    }

    override fun onCompletion(completion: OnCompletion<DatmusicPlayer>) {

    }

    override fun onMetaDataChanged(metaDataChanged: OnMetaDataChanged) {

    }

    override fun updateData(list: List<String>, title: String?) {

    }

    override fun setData(list: List<String>, title: String?) {

    }

    override fun clearRandomAudioPlayed() {

    }

    override fun updatePlaybackState(applier: PlaybackStateCompat.Builder.() -> Unit) {

    }

    override fun setPlaybackState(state: PlaybackStateCompat) {
        mediaSession.setPlaybackState(state)
        state.extras?.let { bundle ->
            mediaSession.setRepeatMode(bundle.getInt(REPEAT_MODE))
            mediaSession.setShuffleMode(bundle.getInt(SHUFFLE_MODE))
        }
    }

    private fun goToStart() {
        isInitialized = false

        stop(byUser = false)

        if (queueManager.queue.isEmpty()) return

        launch {
            //setCurrentAudioId(queueManager.queue.first())
           // queueManager.refreshCurrentAudio()?.apply { setMetaData(this) }
        }
    }
}
