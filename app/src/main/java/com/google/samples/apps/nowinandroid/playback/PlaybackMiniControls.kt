/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.google.samples.apps.nowinandroid.playback


import android.media.metrics.PlaybackStateEvent.STATE_BUFFERING
import android.media.session.PlaybackState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object PlaybackMiniControlsDefaults {
    val height = 56.dp
}

@Composable
fun PlaybackMiniControls(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    PlaybackMiniControls1(contentPadding = contentPadding)
}

@Composable
fun PlaybackMiniControls1(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    height: Dp = PlaybackMiniControlsDefaults.height,
   // playbackConnection: PlaybackConnection = LocalPlaybackConnection.current
) {
  //  val playbackState by rememberFlowWithLifecycle(playbackConnection.playbackState)
    Column {
        var controlsVisible by remember { mutableStateOf(true) }
        var nowPlayingVisible by remember { mutableStateOf(true) }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(height)
                .fillMaxWidth()
                //.background(backgroundColor)
                .onGloballyPositioned {
                    val aspectRatio = it.size.height.toFloat() / it.size.width.toFloat()
                    controlsVisible = aspectRatio < 0.9
                    nowPlayingVisible = aspectRatio < 0.5
                }
                .padding(if (controlsVisible) contentPadding else PaddingValues())
        ) {
        }
        PlaybackProgress(
           // playbackState = playbackState,
            color = MaterialTheme.colors.onBackground,
        )
    }
}

@Composable
private fun PlaybackProgress(
   // playbackState: PlaybackState,
    color: Color,
) {
    val sizeModifier = Modifier
        .height(2.dp)
        .fillMaxWidth()
    when {
       // playbackState.state ==  STATE_BUFFERING-> {

      //  }

        else -> {
            LinearProgressIndicator(
                color = color,
                backgroundColor = color.copy(ProgressIndicatorDefaults.IndicatorBackgroundOpacity),
                modifier = sizeModifier
            )
        }
    }
}


