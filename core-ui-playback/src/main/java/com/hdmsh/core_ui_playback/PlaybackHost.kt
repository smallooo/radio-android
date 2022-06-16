/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.hdmsh.core_ui_playback

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.samples.apps.nowinandroid.common.compose.LocalPlaybackConnection


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlaybackHost(
    viewModel: PlaybackConnectionViewModel = hiltViewModel(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalPlaybackConnection provides viewModel.playbackConnection,
    ) {
        content()
    }
}
