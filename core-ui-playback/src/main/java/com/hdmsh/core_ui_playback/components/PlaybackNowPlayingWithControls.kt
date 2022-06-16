/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.hdmsh.core_ui_playback.components


import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight



object PlaybackNowPlayingDefaults {
    val titleTextStyle @Composable get() = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
    val artistTextStyle @Composable get() = MaterialTheme.typography.subtitle1
}
//
//@Composable
//internal fun PlaybackNowPlayingWithControls(
//    nowPlaying: MediaMetadataCompat,
//    playbackState: PlaybackStateCompat,
//    contentColor: Color,
//    onTitleClick: Callback,
//    onArtistClick: Callback,
//    modifier: Modifier = Modifier,
//    titleTextStyle: TextStyle = PlaybackNowPlayingDefaults.titleTextStyle,
//    artistTextStyle: TextStyle = PlaybackNowPlayingDefaults.artistTextStyle,
//    onlyControls: Boolean = false,
//) {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = modifier.padding(AppTheme.specs.paddingLarge)
//    ) {
//        if (!onlyControls)
//            PlaybackNowPlaying(
//                nowPlaying = nowPlaying,
//                onTitleClick = onTitleClick,
//                onArtistClick = onArtistClick,
//                titleTextStyle = titleTextStyle,
//                artistTextStyle = artistTextStyle,
//            )
//
//        PlaybackProgress(
//            playbackState = playbackState,
//            contentColor = contentColor
//        )
//
//        PlaybackControls(
//            playbackState = playbackState,
//            contentColor = contentColor,
//        )
//    }
//}
//@Composable
//internal fun PlaybackNowPlaying(
//    nowPlaying: MediaMetadataCompat,
//    onTitleClick: Callback,
//    onArtistClick: Callback,
//    modifier: Modifier = Modifier,
//    titleTextStyle: TextStyle = PlaybackNowPlayingDefaults.titleTextStyle,
//    artistTextStyle: TextStyle = PlaybackNowPlayingDefaults.artistTextStyle,
//    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
//) {
//    val title = nowPlaying.title
//    Column(
//        horizontalAlignment = horizontalAlignment,
//        modifier = modifier
//    ) {
//        Text(
//            title.orNA(),
//            style = titleTextStyle,
//            overflow = TextOverflow.Ellipsis,
//            maxLines = 1,
//            modifier = Modifier.simpleClickable(onClick = onTitleClick)
//        )
//        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
//            Text(
//                nowPlaying.artist.orNA(),
//                style = artistTextStyle,
//                overflow = TextOverflow.Ellipsis,
//                maxLines = 1,
//                modifier = Modifier.simpleClickable(onClick = onArtistClick)
//            )
//        }
//    }
//}
//@Composable
//internal fun PlaybackControls(
//    playbackState: PlaybackStateCompat,
//    contentColor: Color,
//    modifier: Modifier = Modifier,
//    smallRippleRadius: Dp = 30.dp,
//    playbackConnection: PlaybackConnection = LocalPlaybackConnection.current
//) {
//    val playbackMode by rememberFlowWithLifecycle(playbackConnection.playbackMode)
//    Row(
//        modifier = modifier.width(288.dp),
//        horizontalArrangement = Arrangement.Center,
//        verticalAlignment = Alignment.CenterVertically,
//    ) {
//        IconButton(
//            onClick = { playbackConnection.mediaController?.toggleShuffleMode() },
//            modifier = Modifier
//                .size(20.dp)
//                .weight(2f),
//            rippleRadius = smallRippleRadius,
//        ) {
//            Icon(
//                painter = rememberVectorPainter(
//                    when (playbackMode.shuffleMode) {
//                        PlaybackStateCompat.SHUFFLE_MODE_NONE -> Icons.Default.Shuffle
//                        PlaybackStateCompat.SHUFFLE_MODE_ALL -> Icons.Default.ShuffleOn
//                        else -> Icons.Default.Shuffle
//                    }
//                ),
//                tint = contentColor,
//                modifier = Modifier.fillMaxSize(),
//                contentDescription = null
//            )
//        }
//
//        Spacer(Modifier.width(AppTheme.specs.paddingLarge))
//
//        IconButton(
//            onClick = { playbackConnection.transportControls?.skipToPrevious() },
//            modifier = Modifier
//                .size(40.dp)
//                .weight(4f),
//            rippleRadius = smallRippleRadius,
//        ) {
//            Icon(
//                painter = rememberVectorPainter(Icons.Default.SkipPrevious),
//                tint = contentColor.disabledAlpha(playbackState.hasPrevious),
//                modifier = Modifier.fillMaxSize(),
//                contentDescription = null
//            )
//        }
//
//        Spacer(Modifier.width(AppTheme.specs.padding))
//
//        IconButton(
//            onClick = { playbackConnection.mediaController?.playPause() },
//            modifier = Modifier
//                .size(80.dp)
//                .weight(8f),
//            rippleRadius = 35.dp,
//        ) {
//            Icon(
//                painter = rememberVectorPainter(
//                    when {
//                        playbackState.isError -> Icons.Filled.ErrorOutline
//                        playbackState.isPlaying -> Icons.Filled.PauseCircleFilled
//                        playbackState.isPlayEnabled -> Icons.Filled.PlayCircleFilled
//                        else -> Icons.Filled.PlayCircleFilled
//                    }
//                ),
//                tint = contentColor,
//                modifier = Modifier.fillMaxSize(),
//                contentDescription = null
//            )
//        }
//
//        Spacer(Modifier.width(AppTheme.specs.padding))
//
//        IconButton(
//            onClick = { playbackConnection.transportControls?.skipToNext() },
//            modifier = Modifier
//                .size(40.dp)
//                .weight(4f),
//            rippleRadius = smallRippleRadius,
//        ) {
//            Icon(
//                painter = rememberVectorPainter(Icons.Default.SkipNext),
//                tint = contentColor.disabledAlpha(playbackState.hasNext),
//                modifier = Modifier.fillMaxSize(),
//                contentDescription = null
//            )
//        }
//
//        Spacer(Modifier.width(AppTheme.specs.paddingLarge))
//
//        IconButton(
//            onClick = { playbackConnection.mediaController?.toggleRepeatMode() },
//            modifier = Modifier
//                .size(20.dp)
//                .weight(2f),
//            rippleRadius = smallRippleRadius,
//        ) {
//            Icon(
//                painter = rememberVectorPainter(
//                    when (playbackMode.repeatMode) {
//                        PlaybackStateCompat.REPEAT_MODE_ONE -> Icons.Default.RepeatOneOn
//                        PlaybackStateCompat.REPEAT_MODE_ALL -> Icons.Default.RepeatOn
//                        else -> Icons.Default.Repeat
//                    }
//                ),
//                tint = contentColor,
//                modifier = Modifier.fillMaxSize(),
//                contentDescription = null
//            )
//        }
//    }
//}
