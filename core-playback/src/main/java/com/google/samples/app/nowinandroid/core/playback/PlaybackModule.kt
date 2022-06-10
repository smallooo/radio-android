/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.google.samples.app.nowinandroid.core.playback

import android.content.ComponentName
import android.content.Context
import com.google.samples.app.nowinandroid.core.playback.players.AudioPlayerImpl
import com.google.samples.app.nowinandroid.core.playback.services.PlayerService
import com.google.samples.apps.nowinandroid.playback.PlaybackConnection
import com.google.samples.apps.nowinandroid.playback.PlaybackConnectionImpl
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
    fun playbackConnection(): PlaybackConnection = PlaybackConnectionImpl()
}
