package com.hdmsh.core_ui_playback

import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.samples.app.nowinandroid.core.playback.*
import com.google.samples.apps.nowinandroid.common.compose.LocalPlaybackConnection
import com.google.samples.apps.nowinandroid.core.model.data.Station
import com.google.samples.apps.nowinandroid.core.navigation.LocalNavigator
import com.google.samples.apps.nowinandroid.core.navigation.Navigator
import com.google.samples.apps.nowinandroid.core.navigation.Screens.LeafScreen

import com.google.samples.apps.nowinandroid.core.ui.Dismissable
import com.google.samples.apps.nowinandroid.core.ui.adaptiveColor
import com.google.samples.apps.nowinandroid.core.ui.component.CoverImage
import com.google.samples.apps.nowinandroid.playback.PLAYBACK_PROGRESS_INTERVAL
import com.google.samples.apps.nowinandroid.playback.PlaybackConnection
import com.hdmsh.common_compose.rememberFlowWithLifecycle
import  com.google.samples.apps.nowinandroid.core.ui.component.IconButton
import com.google.samples.apps.nowinandroid.core.ui.theme.AppTheme

object PlaybackMiniControlsDefaults { val height = 56.dp }

@Composable
fun PlaybackMiniControls(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    playbackConnection: PlaybackConnection = LocalPlaybackConnection.current
) {
    val playbackState by rememberFlowWithLifecycle(playbackConnection.playbackState)
    val nowPlaying by rememberFlowWithLifecycle(playbackConnection.nowPlaying)
    val playingStation by rememberFlowWithLifecycle(playbackConnection.playingStation)

    val visible = (playbackState to nowPlaying).isActive

    AnimatedVisibility(
        visible = visible,
        modifier = modifier.padding(8.dp,0.dp,8.dp,0.dp),
        enter = slideInVertically(initialOffsetY = { it / 2 }),
        exit = slideOutVertically(targetOffsetY = { it / 2 })
    ) {
        PlaybackMiniControls(
            playbackState = playbackState,
            nowPlaying = nowPlaying,
            onPlayPause = { playbackConnection.mediaController?.playPause() },
            // onNavigateToTopLevelDestination,
            playingStation,
            contentPadding = contentPadding
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaybackMiniControls(
    playbackState: PlaybackStateCompat,
    nowPlaying: MediaMetadataCompat,
    onPlayPause: () -> Unit,
    // onNavigateToTopLevelDestination: (TopLevelDestination) -> Unit,
    playingStation: Station,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    height: Dp = PlaybackMiniControlsDefaults.height,
    playbackConnection: PlaybackConnection = LocalPlaybackConnection.current,
    navigator: Navigator = LocalNavigator.current,
) {
    val openPlaybackSheet = { navigator.navigate(LeafScreen.PlaybackSheet().createRoute()) }
    val adaptiveColor by adaptiveColor(nowPlaying.artwork, initial = MaterialTheme.colors.background)
    val backgroundColor = adaptiveColor.color
    val contentColor = adaptiveColor.contentColor

    Dismissable(onDismiss = { playbackConnection.transportControls?.stop()  }) {
        var dragOffset by remember { mutableStateOf(0f) }
        Surface(
            color = Color.Transparent,
            shape = MaterialTheme.shapes.small,
            modifier = modifier
                .padding(horizontal = 0.dp)
                .animateContentSize()
                .combinedClickable(
                    enabled = true,
                    onClick = openPlaybackSheet ,//openPlaybackSheet,
                    onLongClick = onPlayPause,
                    onDoubleClick = onPlayPause
                )
                // open playback sheet on swipe up
                .draggable(
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState(
                        onDelta = {
                            dragOffset = it.coerceAtMost(0f)
                        }
                    ),
                    onDragStarted = {
                       if (dragOffset < 0) openPlaybackSheet()
                    },
                )
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
                        .background(backgroundColor)
                        .onGloballyPositioned {
                            val aspectRatio = it.size.height.toFloat() / it.size.width.toFloat()
                            controlsVisible = aspectRatio < 0.9
                            nowPlayingVisible = aspectRatio < 0.5
                        }
                        .padding(if (controlsVisible) contentPadding else PaddingValues())
                ) {

                    //PlaybackNowPlaying(coverOnly = !nowPlayingVisible, nowPlaying, playingStation)

                    CompositionLocalProvider() {
                        PlaybackNowPlaying(nowPlaying = nowPlaying, playingStation = playingStation, maxHeight = height, coverOnly = !nowPlayingVisible)
                        if (controlsVisible)
                            PlaybackPlayPause(playbackState = playbackState, onPlayPause = onPlayPause)
                    }
                }

                PlaybackProgress(
                    playbackState = playbackState,
                    color = MaterialTheme.colors.onBackground,
                )
            }
        }
    }
}

@Composable
fun PlaybackProgress(
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
private fun RowScope.PlaybackPlayPause(
    playbackState: PlaybackStateCompat,
    size: Dp = 36.dp,
    onPlayPause: () -> Unit
) {
    IconButton(
        onClick = onPlayPause,
        rippleColor = LocalContentColor.current,
        modifier = Modifier.weight(1f)
    ) {
        Icon(
            imageVector = when {
                playbackState.isError -> Icons.Filled.ErrorOutline
                playbackState.isPlaying -> Icons.Filled.Pause
                playbackState.isPlayEnabled -> Icons.Filled.PlayArrow
                else -> Icons.Filled.HourglassBottom
            },
            modifier = Modifier.size(size),
            contentDescription = null
        )
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun RowScope.PlaybackNowPlaying(
    nowPlaying: MediaMetadataCompat,
    playingStation: Station,
    maxHeight: Dp = 200.dp,
    coverOnly: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.weight(if (coverOnly) 3f else 7f),
    ) {


        CoverImage(
            data = nowPlaying.artwork ?: nowPlaying.artworkUri,
            size = maxHeight - 16.dp,
            modifier = Modifier.padding(8.dp)
        )

        if (!coverOnly) {
            // PlaybackPager(nowPlaying = nowPlaying) {
            PlaybackNowPlaying(playingStation)
            // }
        }
    }
}

@Composable
fun animatePlaybackProgress(
    targetValue: Float,
) = animateFloatAsState(
    targetValue = targetValue,
    animationSpec = tween(
        durationMillis = PLAYBACK_PROGRESS_INTERVAL.toInt(),
        easing = FastOutSlowInEasing
    ),
)


@Composable
fun PlaybackNowPlaying(playingStation: Station, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .then(modifier)
    ) {
        Text(
            playingStation.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold)
        )
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                "audio.artist.orNA()",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body2
            )
        }
    }
}



