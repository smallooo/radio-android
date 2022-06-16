/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.hdmsh.core_ui_playback.components

import android.support.v4.media.session.PlaybackStateCompat
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.samples.apps.nowinandroid.common.compose.LocalPlaybackConnection
import com.google.samples.apps.nowinandroid.playback.PlaybackConnection



@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun PlaybackProgress(
    playbackState: PlaybackStateCompat,
    contentColor: Color,
    thumbRadius: Dp = 4.dp,
    playbackConnection: PlaybackConnection = LocalPlaybackConnection.current
) {
//    val progressState by rememberFlowWithLifecycle(playbackConnection.playbackProgress)
    val (draggingProgress, setDraggingProgress) = remember { mutableStateOf<Float?>(null) }

//    Box {
//        PlaybackProgressSlider(
//            playbackState,
//            progressState,
//            draggingProgress,
//            setDraggingProgress,
//            thumbRadius,
//            contentColor
//        )
//        PlaybackProgressDuration(progressState, draggingProgress, thumbRadius)
//    }
}
//@Composable
//internal fun PlaybackProgressSlider(
//    playbackState: PlaybackStateCompat,
//    progressState: PlaybackProgressState,
//    draggingProgress: Float?,
//    setDraggingProgress: (Float?) -> Unit,
//    thumbRadius: Dp,
//    contentColor: Color,
//    bufferedProgressColor: Color = contentColor.copy(alpha = 0.25f),
//    height: Dp = 44.dp,
//    playbackConnection: PlaybackConnection = LocalPlaybackConnection.current
//) {
//    val updatedProgressState by rememberUpdatedState(progressState)
//    val updatedDraggingProgress by rememberUpdatedState(draggingProgress)
//
//    val sliderColors = SliderDefaults.colors(
//        thumbColor = contentColor,
//        activeTrackColor = contentColor,
//        inactiveTrackColor = contentColor.copy(alpha = ContentAlpha.disabled)
//    )
//    val linearProgressMod = Modifier
//        .fillMaxWidth(fraction = .99f) // reduce linearProgressIndicators width to match Slider's
//        .clip(CircleShape) // because Slider is rounded
//
//    val bufferedProgress by animatePlaybackProgress(progressState.bufferedProgress)
//    val isBuffering = playbackState.isBuffering
//    val sliderProgress = progressState.progress
//
//    Box(
//        modifier = Modifier.height(height),
//        contentAlignment = Alignment.Center
//    ) {
//        if (!isBuffering)
//            LinearProgressIndicator(
//                progress = bufferedProgress,
//                color = bufferedProgressColor,
//                backgroundColor = Color.Transparent,
//                modifier = linearProgressMod
//            )
//
//        Slider(
//            value = draggingProgress ?: sliderProgress,
//            onValueChange = {
//                if (!isBuffering) setDraggingProgress(it)
//            },
//            thumbRadius = thumbRadius,
//            colors = sliderColors,
//            modifier = Modifier.alpha(isBuffering.not().toFloat()),
//            onValueChangeFinished = {
//                playbackConnection.transportControls?.seekTo(
//                    (
//                        updatedProgressState.total.toFloat() * (
//                            updatedDraggingProgress
//                                ?: 0f
//                            )
//                        ).roundToLong()
//                )
//                setDraggingProgress(null)
//            }
//        )
//
//        if (isBuffering) {
//            LinearProgressIndicator(
//                progress = 0f,
//                color = contentColor,
//                modifier = linearProgressMod
//            )
//            Delayed(
//                modifier = Modifier
//                    .align(Alignment.Center)
//                    .then(linearProgressMod)
//            ) {
//                LinearProgressIndicator(
//                    color = contentColor,
//                )
//            }
//        }
//    }
//}
//@Composable
//internal fun BoxScope.PlaybackProgressDuration(
//    progressState: PlaybackProgressState,
//    draggingProgress: Float?,
//    thumbRadius: Dp
//) {
//    Row(
//        horizontalArrangement = Arrangement.SpaceBetween,
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(top = thumbRadius)
//            .align(Alignment.BottomCenter)
//    ) {
//        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
//            val currentDuration = when (draggingProgress != null) {
//                true -> (progressState.total.toFloat() * (draggingProgress)).toLong().millisToDuration()
//                else -> progressState.currentDuration
//            }
//            Text(currentDuration, style = MaterialTheme.typography.caption)
//            Text(progressState.totalDuration, style = MaterialTheme.typography.caption)
//        }
//    }
//}
//
//@Composable
//internal fun animatePlaybackProgress(
//    targetValue: Float,
//) = animateFloatAsState(
//    targetValue = targetValue,
//    animationSpec = tween(
//        durationMillis = PLAYBACK_PROGRESS_INTERVAL.toInt(),
//        easing = FastOutSlowInEasing
//    ),
//)
