/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.hdmsh.core_ui_playback.components

import android.support.v4.media.MediaMetadataCompat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.util.lerp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import com.google.samples.apps.nowinandroid.common.compose.LocalPlaybackConnection
import com.google.samples.apps.nowinandroid.playback.PlaybackConnection
import com.hdmsh.common_compose.rememberFlowWithLifecycle
import kotlin.math.absoluteValue


@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun PlaybackPager(
    nowPlaying: MediaMetadataCompat,
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState(),
    playbackConnection: PlaybackConnection = LocalPlaybackConnection.current,
    content: @Composable () -> Unit,
) {
    val playbackQueue by rememberFlowWithLifecycle(playbackConnection.nowPlaying)
   // val playbackCurrentIndex = playbackQueue.currentIndex
//    var lastRequestedPage by remember(playbackQueue, nowPlaying) {
//        mutableStateOf<Int?>(
//            playbackCurrentIndex
//        )
//    }

//    if (!playbackQueue.isValid) {
//        content(nowPlaying.toAudio(), playbackCurrentIndex, modifier)
//        return
//    }
//    LaunchedEffect(Unit) {
//        pagerState.scrollToPage(playbackCurrentIndex)
//    }
//    LaunchedEffect(playbackCurrentIndex, pagerState) {
//        if (playbackCurrentIndex != pagerState.currentPage) {
//            pagerState.animateScrollToPage(playbackCurrentIndex)
//        }
//        snapshotFlow { pagerState.isScrollInProgress }
//            .filter { !it }
//            .map { pagerState.currentPage }
//            .collectLatest { page ->
//                if (lastRequestedPage != page) {
//                    lastRequestedPage = page
//                    playbackConnection.transportControls?.skipToQueueItem(page.toLong())
//                }
//            }
//    }
//    HorizontalPager(
//        count = 1, //playbackQueue.size,
//        modifier = Modifier,
//        state = pagerState,
//        key = { playbackConnection.playingStation },//{ playbackQueue.audios.getOrNull(it) ?: it },
//    ) { page ->
//    //    val currentStation = playbackConnection.playingStation
//
////        val pagerMod = Modifier.graphicsLayer {
////            val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
////            // TODO: report to upstream if can be reproduced in isolation
////            if (pageOffset.isNaN()) {
////                return@graphicsLayer
////            }
////
////            lerp(
////                start = 0.85f,
////                stop = 1f,
////                fraction = 1f - pageOffset.coerceIn(0f, 1f)
////            ).also { scale ->
////                scaleX = scale
////                scaleY = scale
////            }
////            alpha = lerp(
////                start = 0.5f,
////                stop = 1f,
////                fraction = 1f - pageOffset.coerceIn(0f, 1f)
////            )
////        }
//        content( )
//    }
}