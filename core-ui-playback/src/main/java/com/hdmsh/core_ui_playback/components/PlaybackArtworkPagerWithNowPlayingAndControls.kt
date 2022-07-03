/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.hdmsh.core_ui_playback.components

import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.dmhsh.samples.app.nowinandroid.core.playback.artworkUri

import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.hdmsh.core_ui_playback.PlaybackMiniControlsDefaults.height
import kotlinx.coroutines.NonDisposableHandle.parent
import javax.security.auth.callback.Callback


@OptIn(ExperimentalPagerApi::class)
@Composable
fun PlaybackArtworkPagerWithNowPlayingAndControls(
    nowPlaying: MediaMetadataCompat,
    playbackState: PlaybackStateCompat,
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colors.onBackground,
    titleTextStyle: TextStyle = PlaybackNowPlayingDefaults.titleTextStyle,
    artistTextStyle: TextStyle = PlaybackNowPlayingDefaults.artistTextStyle,
    pagerState: PagerState = rememberPagerState(),
    onArtworkClick: Callback? = null,
    //viewModel: PlaybackViewModel = hiltViewModel(),
) {
    Box(modifier = modifier) {
        //val (pager, nowPlayingControls) = createRefs()
        PlaybackPager(
           // nowPlaying = nowPlaying,
            pagerState = pagerState,
//            modifier = Modifier.constrainAs(pager) {
//                centerHorizontallyTo(parent)
//                top.linkTo(parent.top)
//                bottom.linkTo(nowPlayingControls.top)
//                height = Dimension.fillToConstraints
//            }
        ) { _ , _ ->
            val currentArtwork = nowPlaying.artworkUri
            PlaybackArtwork(
                artwork = currentArtwork,
                contentColor = contentColor,
                nowPlaying = nowPlaying,
                onClick = {  },
                modifier = Modifier,
            )
        }
        PlaybackNowPlayingWithControls(
            nowPlaying = nowPlaying,
            playbackState = playbackState,
            contentColor = contentColor,
            titleTextStyle = titleTextStyle,
            artistTextStyle = artistTextStyle,
            onTitleClick = {},
            onArtistClick = {},
//            modifier = Modifier.constrainAs(nowPlayingControls) {
//                centerHorizontallyTo(parent)
//                bottom.linkTo(parent.bottom)
//                height = Dimension.fillToConstraints
//            }
        )
    }
}
