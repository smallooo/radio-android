/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.google.samples.apps.nowinandroid.playback

import android.media.session.PlaybackState
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.samples.app.nowinandroid.core.playback.isActive
import com.google.samples.app.nowinandroid.core.playback.isBuffering
import com.google.samples.app.nowinandroid.core.playback.playPause
import com.google.samples.apps.nowinandroid.R
import com.google.samples.apps.nowinandroid.core.compose.LocalPlaybackConnection

object PlaybackMiniControlsDefaults { val height = 56.dp }

@Composable
fun PlaybackMiniControls(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    playbackConnection: PlaybackConnection = LocalPlaybackConnection.current,
) {
    val playbackState by rememberFlowWithLifecycle(playbackConnection.playbackState)
    val nowPlaying by rememberFlowWithLifecycle(playbackConnection.nowPlaying)

    val visible = playbackState.state == 3 || playbackState.state == 6

    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = slideInVertically(initialOffsetY = { it / 2 }),
        exit = slideOutVertically(targetOffsetY = { it / 2 })
    ) {
        PlaybackMiniControls(
            playbackState = playbackState,
            nowPlaying = nowPlaying,
            onPlayPause = { playbackConnection.mediaController?.playPause() },
            contentPadding = contentPadding)
    }
}

@Composable
fun PlaybackMiniControls(
    playbackState : PlaybackStateCompat,
    nowPlaying : MediaMetadataCompat,
    onPlayPause: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    height: Dp = PlaybackMiniControlsDefaults.height,
) {
    Column {
        var controlsVisible by remember { mutableStateOf(true) }
        var nowPlayingVisible by remember { mutableStateOf(true) }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(height)
                .fillMaxWidth()
                .onGloballyPositioned {
                    val aspectRatio = it.size.height.toFloat() / it.size.width.toFloat()
                    controlsVisible = aspectRatio < 0.9
                    nowPlayingVisible = aspectRatio < 0.5
                }
                .padding(if (controlsVisible) contentPadding else PaddingValues())
        ) {

                PlaybackNowPlaying( coverOnly = !nowPlayingVisible)
        }

            PlaybackProgress(
                playbackState = playbackState,
                color = MaterialTheme.colors.onBackground,
            )
    }
}

@Composable
private fun PlaybackProgress(
    playbackState: PlaybackStateCompat,
    color: Color,
    playbackConnection: PlaybackConnection = LocalPlaybackConnection.current,
) {
    val progressState by rememberFlowWithLifecycle(playbackConnection.playbackProgress)
    val sizeModifier = Modifier
        .height(2.dp)
        .fillMaxWidth()
    when {
        playbackState.isBuffering -> {
            LinearProgressIndicator(
                color = color,
                modifier = sizeModifier
            )
        }
        else -> {
            val progress by animatePlaybackProgress(progressState.progress)
            LinearProgressIndicator(
                progress = progress,
                color = color,
                backgroundColor = color.copy(ProgressIndicatorDefaults.IndicatorBackgroundOpacity),
                modifier = sizeModifier
            )
        }
    }
}

@Composable
internal fun animatePlaybackProgress(
    targetValue: Float,
) = animateFloatAsState(
    targetValue = targetValue,
    animationSpec = tween(
        durationMillis = PLAYBACK_PROGRESS_INTERVAL.toInt(),
        easing = FastOutSlowInEasing
    ),
)

@Composable
private fun RowScope.PlaybackNowPlaying(
    modifier: Modifier = Modifier,
    coverOnly: Boolean = false,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.weight(if (coverOnly) 3f else 7f),
    ) {
        Image(painter = painterResource(id = R.drawable.ic_100tb), contentDescription = "")
    }
}



