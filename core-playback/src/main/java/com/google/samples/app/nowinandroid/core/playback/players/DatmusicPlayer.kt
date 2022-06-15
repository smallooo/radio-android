package com.google.samples.app.nowinandroid.core.playback.players

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.media.session.PlaybackState
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import com.google.samples.app.nowinandroid.core.playback.*
import com.google.samples.app.nowinandroid.core.playback.models.toMediaMetadata
import com.google.samples.apps.nowinandroid.core.domain.CoverImageSize
import com.google.samples.apps.nowinandroid.core.imageloading.getBitmap
import com.google.samples.apps.nowinandroid.core.model.data.Station
import com.google.samples.apps.nowinandroid.core.playback.R
import com.google.samples.apps.nowinandroid.core.util.extensions.plus
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
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
    fun playAudio()
    //suspend fun playAudio(id: String, index: Int? = null)
    suspend fun playRadio(station : Station)
    //fun seekTo(position: Long)
    //fun fastForward()
   // fun rewind()
    fun pause(extras: Bundle = bundleOf(BY_UI_KEY to true))
    //suspend fun nextAudio(): String?
    //suspend fun repeatAudio()
    //suspend fun repeatQueue()
    //suspend fun previousAudio()
   // fun playNext(id: String)
   // suspend fun skipTo(position: Int)
   // fun removeFromQueue(position: Int)
    //fun removeFromQueue(id: String)
   // fun swapQueueAudios(from: Int, to: Int)
    fun stop(byUser: Boolean)
    fun release()
    fun onPlayingState(playing: OnIsPlaying<DatmusicPlayer>)
    fun onPrepared(prepared: OnPrepared<DatmusicPlayer>)
    fun onError(error: OnError<DatmusicPlayer>)
    fun onCompletion(completion: OnCompletion<DatmusicPlayer>)
    fun onMetaDataChanged(metaDataChanged: OnMetaDataChanged)
    fun updatePlaybackState(applier: PlaybackStateCompat.Builder.() -> Unit = {})
    fun setPlaybackState(state: PlaybackStateCompat)
//    fun setShuffleMode(shuffleMode: Int)
//    fun updateData(list: List<String> = emptyList(), title: String? = null)
    fun setData(list: List<String> = emptyList(), title: String? = null)
  //  suspend fun setDataFromMediaId(_mediaId: String, extras: Bundle = bundleOf())
  //  suspend fun saveQueueState()
  //  suspend fun restoreQueueState()
  //  fun clearRandomAudioPlayed()
  //  fun setCurrentAudioId(audioId: String, index: Int? = null)
  //  fun shuffleQueue(isShuffle: Boolean)
}

@Singleton
class DatmusicPlayerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val audioPlayer: AudioPlayerImpl,
    private val queueManager: AudioQueueManagerImpl,
    private val audioFocusHelper: AudioFocusHelperImpl,
    //private val audiosRepo: AudiosRepo,
    private val mediaQueueBuilder: MediaQueueBuilder,
    //private val preferences: PreferencesStore,
    //private val analytics: FirebaseAnalytics,
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

    private val pendingIntent = PendingIntent.getBroadcast(context, 0, Intent(Intent.ACTION_MEDIA_BUTTON), FLAG_IMMUTABLE)

    private val mediaSession = MediaSessionCompat(context, "HiRadio", null, pendingIntent).apply {
        setCallback(
            MediaSessionCallback(this, this@DatmusicPlayerImpl, audioFocusHelper)
        )
        setPlaybackState(stateBuilder.build())
        val sessionIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        val sessionActivityPendingIntent = PendingIntent.getActivity(context, 0, sessionIntent, FLAG_IMMUTABLE)
        setSessionActivity(sessionActivityPendingIntent)
        isActive = true
    }

    init {
        queueManager.setMediaSession(mediaSession)
        audioPlayer.onPrepared {
            preparedCallback(this@DatmusicPlayerImpl)
            launch {
                if (!mediaSession.isPlaying()) audioPlayer.seekTo(mediaSession.position())
                playAudio()
            }
        }

        audioPlayer.onCompletion {
//            completionCallback(this@DatmusicPlayerImpl)
//            val controller = getSession().controller
//            when (controller.repeatMode) {
//                PlaybackStateCompat.REPEAT_MODE_ONE -> controller.transportControls.sendCustomAction(REPEAT_ONE, null)
//                PlaybackStateCompat.REPEAT_MODE_ALL -> controller.transportControls.sendCustomAction(REPEAT_ALL, null)
//                else -> launch { if (nextAudio() == null) goToStart() }
//            }
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
            Timber.d("Couldn't pause player: ${audioPlayer.isPlaying()}, $isInitialized")
        }
    }

    override fun playAudio() {
        if (isInitialized) {
            audioPlayer.play()
            return
        }
    }



    override suspend fun playRadio(station : Station) {
        audioFocusHelper.requestPlayback()
        isInitialized = false

        updatePlaybackState { setState(PlaybackStateCompat.STATE_PLAYING, mediaSession.position(), 1F) }
        setMetaData(station)
        isInitialized = true
        audioPlayer.setSource(station.url_resolved.toUri(), false)
        audioPlayer.prepare()
        updatePlaybackState { setState(mediaSession.controller.playbackState.state, 1, 1F) }
    }

    override fun stop(byUser: Boolean) {
        updatePlaybackState {
            setState(if (byUser) PlaybackStateCompat.STATE_NONE else PlaybackStateCompat.STATE_STOPPED, 0, 1F)
        }
        isInitialized = false
        audioPlayer.stop()
        isPlayingCallback(false, byUser)
        queueManager.clear()
        //launch { saveQueueState() }
    }

    override fun release() {
        mediaSession.apply {
            isActive = false
            release()
        }
        audioPlayer.release()
        queueManager.clear()
    }

    override fun onPlayingState(playing: OnIsPlaying<DatmusicPlayer>) {
        this.isPlayingCallback = playing
    }

    override fun onPrepared(prepared: OnPrepared<DatmusicPlayer>) {
        this.preparedCallback = prepared
    }

    override fun onError(error: OnError<DatmusicPlayer>) {
        this.errorCallback = error
    }

    override fun onCompletion(completion: OnCompletion<DatmusicPlayer>) {
        this.completionCallback = completion
    }

    override fun onMetaDataChanged(metaDataChanged: OnMetaDataChanged) {
        this.metaDataChangedCallback = metaDataChanged
    }

    override fun updatePlaybackState(applier: PlaybackStateCompat.Builder.() -> Unit) {
        applier(stateBuilder)
        stateBuilder.setExtras(
            stateBuilder.build().extras + bundleOf(
                QUEUE_CURRENT_INDEX to queueManager.currentAudioIndex,
                QUEUE_HAS_PREVIOUS to (queueManager.previousAudioIndex != null),
                QUEUE_HAS_NEXT to (queueManager.nextAudioIndex != null),
            )
        )
        setPlaybackState(stateBuilder.build())
    }

    override fun setPlaybackState(state: PlaybackStateCompat) {
        mediaSession.setPlaybackState(state)
        state.extras?.let { bundle ->
            mediaSession.setRepeatMode(bundle.getInt(REPEAT_MODE))
            mediaSession.setShuffleMode(bundle.getInt(SHUFFLE_MODE))
        }
    }

    override fun setData(list: List<String>, title: String?) {
        // reset shuffle mode on new data
        getSession().setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_NONE)
        updatePlaybackState {
            setExtras(bundleOf(SHUFFLE_MODE to PlaybackStateCompat.SHUFFLE_MODE_NONE))
        }

        queueManager.queue = list
        queueManager.queueTitle = title ?: ""
    }

    private fun goToStart() {
        isInitialized = false
        stop(byUser = false)
        if (queueManager.queue.isEmpty()) return
        launch {
           // setCurrentAudioId(queueManager.queue.first())
          //  queueManager.refreshCurrentAudio()?.apply { setMetaData(this) }
        }
    }

    private fun setMetaData(station : Station) {
        val player = this
        val station:Station = station
        launch {
            val mediaMetadata = station.toMediaMetadata(metadataBuilder).apply {
                val artworkFromFile = station.favicon
                if (artworkFromFile != null) {
                   // putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, artworkFromFile)
                }
            }
            mediaSession.setMetadata(mediaMetadata.build())
            metaDataChangedCallback(player)

            // cover image is applied separately to avoid delaying metadata setting while fetching bitmap from network
            val smallCoverBitmap = context.getBitmap(station.favicon, CoverImageSize.SMALL.maxSize)
            val updatedMetadata = mediaMetadata.apply { putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, smallCoverBitmap) }.build()
            mediaSession.setMetadata(updatedMetadata)
            metaDataChangedCallback(player)
        }
    }

   // private fun logEvent(event: String, mediaId: String = queueManager.currentAudioId) = analytics.event("player.$event", mapOf("mediaId" to mediaId))
}
