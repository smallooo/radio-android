package com.hdmsh.core_ui_playback

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
import androidx.compose.material.*
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
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.samples.app.nowinandroid.core.playback.isBuffering
import com.google.samples.app.nowinandroid.core.playback.playPause
import com.google.samples.apps.nowinandroid.common.compose.LocalPlaybackConnection
import com.google.samples.apps.nowinandroid.core.model.data.Station
import com.google.samples.apps.nowinandroid.playback.PLAYBACK_PROGRESS_INTERVAL
import com.google.samples.apps.nowinandroid.playback.PlaybackConnection
import com.hdmsh.common_compose.rememberFlowWithLifecycle
import com.hdmsh.core_ui_playback.components.PlaybackPager


object PlaybackMiniControlsDefaults { val height = 56.dp }

@Composable
fun PlaybackMiniControls(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    //onNavigateToTopLevelDestination: (TopLevelDestination) -> Unit,
    playbackConnection: PlaybackConnection = LocalPlaybackConnection.current,
) {
    val playbackState by rememberFlowWithLifecycle(playbackConnection.playbackState)
    val nowPlaying by rememberFlowWithLifecycle(playbackConnection.nowPlaying)
    val playingStation by rememberFlowWithLifecycle(playbackConnection.playingStation)

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
           // onNavigateToTopLevelDestination,
            playingStation,
            contentPadding = contentPadding)
    }
}

@Composable
fun PlaybackMiniControls(
    playbackState : PlaybackStateCompat,
    nowPlaying : MediaMetadataCompat,
    onPlayPause: () -> Unit,
   // onNavigateToTopLevelDestination: (TopLevelDestination) -> Unit,
    playingStation: Station,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    height: Dp = PlaybackMiniControlsDefaults.height,

    ) {

//    Dismissable(onDismiss = { playbackConnection.transportControls?.stop() }) {
//        var dragOffset by remember { mutableStateOf(0f) }
//        Surface(
//            color = Color.Transparent,
//            shape = MaterialTheme.shapes.small,
//            modifier = modifier
//              //  .padding(horizontal = AppTheme.specs.paddingSmall)
//                .animateContentSize()
////                .combinedClickable(
////                    enabled = true,
////                    onClick = onNavigateToTopLevelDestination(),
////                    onLongClick = onPlayPause,
////                    onDoubleClick = onPlayPause
////                )
//                // open playback sheet on swipe up
//                .draggable(
//                    orientation = Orientation.Vertical,
//                    state = rememberDraggableState(
//                        onDelta = {
//                            dragOffset = it.coerceAtMost(0f)
//                        }
//                    ),
//                    onDragStarted = {
//                    //    if (dragOffset < 0) onNavigateToTopLevelDestination(TOP_LEVEL_DESTINATIONS.get(2))
//                    },
//                )
//        ) {
//
//            }
//        }

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

                PlaybackNowPlaying( coverOnly = !nowPlayingVisible, nowPlaying,playingStation)
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

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun RowScope.PlaybackNowPlaying(
    coverOnly: Boolean = false,
    nowPlaying: MediaMetadataCompat,
    playingStation: Station,
    maxHeight: Dp = 200.dp,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.weight(if (coverOnly) 3f else 7f),
    ) {


        Image(painter = painterResource(id = R.drawable.ic_hourglass_empty), contentDescription = "")

        if(!coverOnly) {
           // PlaybackPager(nowPlaying = nowPlaying) {
                PlaybackNowPlaying(playingStation)
           // }
        }
    }
}

@Composable
private fun PlaybackNowPlaying(playingStation: Station, modifier: Modifier = Modifier) {
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



