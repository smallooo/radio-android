/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.hdmsh.core_ui_playback.components


import android.annotation.SuppressLint
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dmhsh.samples.app.nowinandroid.core.playback.*
import com.dmhsh.samples.app.nowinandroid.core.playback.*
import com.dmhsh.samples.apps.nowinandroid.common.compose.LocalPlaybackConnection
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.IconButton
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.simpleClickable
import com.dmhsh.samples.apps.nowinandroid.core.ui.extensions.Callback
import com.dmhsh.samples.apps.nowinandroid.core.ui.theme.AppTheme
import com.dmhsh.samples.apps.nowinandroid.core.ui.theme.disabledAlpha
import com.dmhsh.samples.apps.nowinandroid.playback.PlaybackConnection
import com.hdmsh.common_compose.rememberFlowWithLifecycle
import com.hdmsh.core_ui_playback.components.PlaybackNowPlayingDefaults.artistTextStyle
import com.hdmsh.core_ui_playback.components.PlaybackNowPlayingDefaults.titleTextStyle
import androidx.compose.ui.text.TextStyle
import com.dmhsh.samples.apps.nowinandroid.core.model.data.Station
import com.dmhsh.samples.apps.nowinandroid.core.ui.extensions.orNA


object PlaybackNowPlayingDefaults {
    val titleTextStyle @Composable get() = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
    val artistTextStyle @Composable get() = MaterialTheme.typography.subtitle1
}

@Composable
internal fun PlaybackNowPlayingWithControls(
    playbackConnection: PlaybackConnection,
    nowPlaying: MediaMetadataCompat,
    playbackState: PlaybackStateCompat,
    contentColor: Color,
    onTitleClick: Callback,
    onArtistClick: Callback,
    modifier: Modifier = Modifier,
    titleTextStyle: TextStyle = PlaybackNowPlayingDefaults.titleTextStyle,
    artistTextStyle: TextStyle = PlaybackNowPlayingDefaults.artistTextStyle,
    onlyControls: Boolean = false,
    onFavoriteClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(28.dp)  //(AppTheme.specs.paddingLarge)
    ) {
        if (!onlyControls)
            PlaybackNowPlaying(
                nowPlaying = nowPlaying,
                onTitleClick = onTitleClick,
                onArtistClick = onArtistClick,
                titleTextStyle = titleTextStyle,
                artistTextStyle = artistTextStyle,
            )

        PlaybackProgress(
            playbackState = playbackState,
            contentColor = contentColor
        )

        PlaybackControls(
            playbackState = playbackState,
            contentColor = contentColor,
            onFavoriteClick = onFavoriteClick
        )
    }
}
@Composable
internal fun PlaybackNowPlaying(
    nowPlaying: MediaMetadataCompat,
    onTitleClick: Callback,
    onArtistClick: Callback,
    modifier: Modifier = Modifier,
    titleTextStyle: TextStyle = PlaybackNowPlayingDefaults.titleTextStyle,
    artistTextStyle: TextStyle = PlaybackNowPlayingDefaults.artistTextStyle,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
) {
    Column(
        horizontalAlignment = horizontalAlignment,
        modifier = modifier
    ) {
//        val title = nowPlaying.title
//        Text(
//            title.orNA(),
//            style = titleTextStyle,
//            overflow = TextOverflow.Ellipsis,
//            maxLines = 1,
//            modifier = Modifier.simpleClickable(onClick = onTitleClick)
//        )


//        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
//            Text(
//                nowPlaying.artist.toString(),
//                style = artistTextStyle,
//                overflow = TextOverflow.Ellipsis,
//                maxLines = 1,
//                modifier = Modifier.simpleClickable(onClick = onArtistClick)
//            )
//        }
    }
}
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
internal fun PlaybackControls(
    playbackState: PlaybackStateCompat,
    contentColor: Color,
    modifier: Modifier = Modifier,
    smallRippleRadius: Dp = 30.dp,
    playbackConnection: PlaybackConnection = LocalPlaybackConnection.current,
    onFavoriteClick: () -> Unit
) {
    var favorited by remember { mutableStateOf(playbackConnection.playingStation.value.favorited)}

    Row(
        modifier = modifier.width(288.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = {  },
            modifier = Modifier
                .size(39.dp)
                .weight(5f),
            rippleRadius = smallRippleRadius,
        ) {
//            Icon(
//                painter = rememberVectorPainter(
//                    when (PlaybackStateCompat.SHUFFLE_MODE_NONE ) {
//                        PlaybackStateCompat.SHUFFLE_MODE_NONE -> Icons.Default.Shuffle
//                        PlaybackStateCompat.SHUFFLE_MODE_ALL -> Icons.Default.ShuffleOn
//                        else -> Icons.Default.Shuffle
//                    }
//                ),
//                tint = contentColor,
//                modifier = Modifier.fillMaxSize(),
//                contentDescription = null
//            )
        }

       // Spacer(Modifier.width(AppTheme.specs.paddingLarge))

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

    //   Spacer(Modifier.width(AppTheme.specs.padding))

        IconButton(
            onClick = { playbackConnection.mediaController?.playPause() },
            modifier = Modifier
                .size(80.dp)
                .weight(8f),
            rippleRadius = 35.dp,
        ) {
            Icon(
                painter = rememberVectorPainter(
                    when {
                        playbackState.isError -> Icons.Filled.ErrorOutline
                        playbackState.isPlaying -> Icons.Filled.PauseCircleFilled
                        playbackState.isPlayEnabled -> Icons.Filled.PlayCircleFilled
                        else -> Icons.Filled.PlayCircleFilled
                    }
                ),
                tint = contentColor,
                modifier = Modifier.fillMaxSize(),
                contentDescription = null
            )
        }

        Spacer(Modifier.width(AppTheme.specs.padding))

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

       // Spacer(Modifier.width(AppTheme.specs.paddingLarge))

        IconButton(
            onClick = {
                favorited = !favorited
                onFavoriteClick()
                      },
            modifier = Modifier
                .size(39.dp)
                .weight(5f),
            rippleRadius = smallRippleRadius,
        ) {
            Icon(
                imageVector = if (favorited) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                tint = contentColor,
                modifier = Modifier.fillMaxSize(),
                contentDescription = null
            )
        }
    }
}
