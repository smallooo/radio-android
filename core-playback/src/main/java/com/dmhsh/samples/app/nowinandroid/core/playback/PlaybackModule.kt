/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.dmhsh.samples.app.nowinandroid.core.playback

import android.content.ComponentName
import android.content.Context
import com.dmhsh.samples.app.nowinandroid.core.playback.players.AudioPlayerImpl
import com.dmhsh.samples.app.nowinandroid.core.playback.players.DatmusicPlayer
import com.dmhsh.samples.app.nowinandroid.core.playback.players.DatmusicPlayerImpl
import com.dmhsh.samples.app.nowinandroid.core.playback.services.PlayerService
import com.dmhsh.samples.apps.nowinandroid.playback.PlaybackConnection
import com.dmhsh.samples.apps.nowinandroid.playback.PlaybackConnectionImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class PlaybackModule {

    @Provides
    @Singleton
    fun playbackConnection(
        @ApplicationContext context: Context,
        audioPlayer: AudioPlayerImpl,
        radioPlayer: DatmusicPlayerImpl
    ): PlaybackConnection = PlaybackConnectionImpl(
        context = context,
        serviceComponent = ComponentName(context, PlayerService::class.java),
        audioPlayer = audioPlayer, radioPlayer = radioPlayer)
}
