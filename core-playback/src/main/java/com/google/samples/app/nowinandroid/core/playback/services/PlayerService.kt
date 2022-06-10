/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.google.samples.app.nowinandroid.core.playback.services

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import androidx.media.MediaBrowserServiceCompat
import com.google.samples.app.nowinandroid.core.playback.MediaQueueBuilder
import com.google.samples.app.nowinandroid.core.playback.models.MediaId
import com.google.samples.app.nowinandroid.core.playback.models.MediaId.Companion.CALLER_OTHER
import com.google.samples.app.nowinandroid.core.playback.models.MediaId.Companion.CALLER_SELF
import com.google.samples.app.nowinandroid.core.playback.models.toMediaId
import com.google.samples.app.nowinandroid.core.playback.players.DatmusicPlayerImpl
import com.google.samples.apps.nowinandroid.core.util.CoroutineDispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class PlayerService : MediaBrowserServiceCompat(), CoroutineScope by MainScope() {

    companion object {
        var IS_FOREGROUND = false
    }

    @Inject
    protected lateinit var dispatchers: CoroutineDispatchers

    @Inject
    protected lateinit var datmusicPlayer: DatmusicPlayerImpl

    @Inject
    protected lateinit var mediaQueueBuilder: MediaQueueBuilder

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot? {
        val caller = if (clientPackageName == applicationContext.packageName) CALLER_SELF else CALLER_OTHER
        return BrowserRoot(MediaId("-1", caller = caller).toString(), null)
    }

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>) {
        result.detach()
        launch {
            val itemList = withContext(dispatchers.io) { loadChildren(parentId) }
            result.sendResult(itemList)
        }
    }

    private suspend fun loadChildren(parentId: String): MutableList<MediaBrowserCompat.MediaItem> {
        val list = mutableListOf<MediaBrowserCompat.MediaItem>()
        val mediaId = parentId.toMediaId()
        //list.addAll(mediaQueueBuilder.buildAudioList(mediaId).toMediaItems())
        return list
    }
}
