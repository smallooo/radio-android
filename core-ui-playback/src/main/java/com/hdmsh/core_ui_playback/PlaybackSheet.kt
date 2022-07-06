/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.hdmsh.core_ui_playback

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

import com.google.accompanist.pager.ExperimentalPagerApi
import com.dmhsh.samples.app.nowinandroid.core.playback.NONE_PLAYBACK_STATE
import com.dmhsh.samples.app.nowinandroid.core.playback.artwork
import com.dmhsh.samples.app.nowinandroid.core.playback.isIdle
import com.dmhsh.samples.apps.nowinandroid.common.compose.LocalPlaybackConnection
import com.dmhsh.samples.apps.nowinandroid.common.compose.LocalScaffoldState
import com.dmhsh.samples.apps.nowinandroid.core.model.data.Station
import com.dmhsh.samples.apps.nowinandroid.core.navigation.LocalNavigator
import com.dmhsh.samples.apps.nowinandroid.core.navigation.Navigator
import com.dmhsh.samples.apps.nowinandroid.core.ui.ADAPTIVE_COLOR_ANIMATION
import com.dmhsh.samples.apps.nowinandroid.core.ui.adaptiveColor
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.DismissableSnackbarHost
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.MoreVerticalIcon

import com.dmhsh.samples.apps.nowinandroid.core.ui.component.isWideLayout
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.simpleClickable
import com.dmhsh.samples.apps.nowinandroid.core.ui.extensions.Callback
import com.dmhsh.samples.apps.nowinandroid.core.ui.media.radioStations.AudioActionHandler
import com.dmhsh.samples.apps.nowinandroid.core.ui.media.radioStations.LocalAudioActionHandler
import com.dmhsh.samples.apps.nowinandroid.core.ui.media.radioStations.audioActionHandler
import com.dmhsh.samples.apps.nowinandroid.core.ui.theme.AppTheme
import com.dmhsh.samples.apps.nowinandroid.core.ui.theme.RadioTheme
import com.dmhsh.samples.apps.nowinandroid.core.ui.theme.plainBackgroundColor
import com.dmhsh.samples.apps.nowinandroid.playback.PlaybackConnection
import com.hdmsh.common_compose.rememberFlowWithLifecycle
import com.hdmsh.core_ui_playback.components.PlaybackArtworkPagerWithNowPlayingAndControls
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch


@Composable
fun PlaybackSheet(
    // override local theme color palette because we want simple colors for menus n' stuff
    //sheetTheme: ThemeState = LocalThemeState.current.copy(colorPalettePreference = ColorPalettePreference.Black),
    navigator: Navigator = LocalNavigator.current,
) {
    val listState = rememberLazyListState()
    val queueListState = rememberLazyListState()
    val coroutine = rememberCoroutineScope()

    val scrollToTop: Callback = {
        coroutine.launch {
            listState.animateScrollToItem(0)
        }
    }

    val audioActionHandler = audioActionHandler()
    CompositionLocalProvider(LocalAudioActionHandler provides audioActionHandler) {
        RadioTheme( changeSystemBar = false) {
            PlaybackSheetContent(
                onClose = { navigator.goBack() },
                scrollToTop = scrollToTop,
                listState = listState,
                queueListState = queueListState,
            )
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun PlaybackSheetContent(
    onClose: Callback,
    scrollToTop: Callback,
    listState: LazyListState,
    queueListState: LazyListState,
    scaffoldState: ScaffoldState = rememberScaffoldState(snackbarHostState = LocalScaffoldState.current.snackbarHostState),
    playbackConnection: PlaybackConnection = LocalPlaybackConnection.current,
   viewModel: PlaybackViewModel = hiltViewModel(),
) {
    val playbackState by rememberFlowWithLifecycle(playbackConnection.playbackState)

    val nowPlaying by rememberFlowWithLifecycle(playbackConnection.nowPlaying)
    // val pagerState = rememberPagerState(playbackQueue.currentIndex)

    val adaptiveColor by adaptiveColor(
        nowPlaying.artwork,
        initial = MaterialTheme.colors.onBackground
    )
    val contentColor by animateColorAsState(adaptiveColor.color, ADAPTIVE_COLOR_ANIMATION)

    LaunchedEffect(playbackConnection) {
        playbackConnection.playbackState
            .filter { it != NONE_PLAYBACK_STATE }
            .collectLatest { if (it.isIdle) onClose() }
    }



        BoxWithConstraints {
        val isWideLayout = isWideLayout()
        val maxWidth = maxWidth
        Row(Modifier.fillMaxSize()) {
            if (isWideLayout) {
//                ResizablePlaybackQueue(
//                    maxWidth = maxWidth,
//                    playbackQueue = playbackQueue,
//                    queueListState = queueListState,
//                    scrollToTop = scrollToTop
//                )
            }

            Scaffold(
                backgroundColor = Color.Transparent,
                modifier = Modifier
                    .background(adaptiveColor.gradient)
                    .weight(1f),
                scaffoldState = scaffoldState,
                snackbarHost = { DismissableSnackbarHost(it, modifier = Modifier.navigationBarsPadding()) },
            ) {
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(8.dp),
                ) {
                    item {
                        PlaybackSheetTopBar(
//                            playbackQueue = playbackQueue,
                            viewModel,
                            onClose = onClose,
                            onTitleClick = {},
//                            onSaveQueueAsPlaylist = viewModel::saveQueueAsPlaylist
                        )
                        Spacer(Modifier.height(4.dp))
                    }


                    item {
                        PlaybackArtworkPagerWithNowPlayingAndControls(
                            nowPlaying = nowPlaying,
                            playbackState = playbackState,
                            //pagerState = pagerState,
                            contentColor = contentColor,
                           // viewModel = viewModel,
                            modifier = Modifier.fillParentMaxHeight(0.8f),
                        )
                    }

//                    if (viewModel.getPlayBackConnection().homepage.length > 0)
//                        item {
//                            PlaybackAudioInfo(viewModel.getPlayBackConnection().homepage)
//                        }

//                    if (!isWideLayout) {
//                        playbackQueueLabel()
//                        playbackQueue(
//                           // playbackQueue = playbackQueue,
//                            scrollToTop = scrollToTop,
//                            playbackConnection = playbackConnection,
//                        )
//                    }
                }
            }
        }
    }
}

private fun LazyListScope.playbackQueueLabel(modifier: Modifier = Modifier) {
    item {
        Row(modifier = modifier.fillMaxWidth()) {
            Text(
                text = "Recommend For You",
                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(AppTheme.specs.padding)
            )
        }
    }
}

@Composable
private fun PlaybackSheetTopBar(
    viewModel: PlaybackViewModel,
    onClose: Callback,
    onTitleClick: Callback,
//    onSaveQueueAsPlaylist: Callback,
) {
    TopAppBar(
        elevation = 0.dp,
        backgroundColor = Color.Transparent,
        title = {
            PlaybackSheetTopBarTitle(viewModel)
                },
        actions = {
            PlaybackSheetTopBarActions()
                  },
        navigationIcon = {
            IconButton(onClick = onClose ) {
                Icon(
                    rememberVectorPainter(Icons.Default.KeyboardArrowDown),
                    modifier = Modifier.size(36.dp),
                    contentDescription = null,
                )
            }
        },
    )
}

@Composable
private fun PlaybackSheetTopBarTitle(
    //playbackQueue: PlaybackQueue,
    //onTitleClick: Callback,
    viewModel: PlaybackViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .offset(x = -8.dp) // idk why this is needed for centering
            .simpleClickable(onClick = {})
    ) {
        val context = LocalContext.current
        val queueTitle = viewModel.getPlayBackConnection().homepage
        Text(
            text = queueTitle,
            style = MaterialTheme.typography.overline.copy(fontWeight = FontWeight.Light),
            maxLines = 1,
        )
        Text(
            text = viewModel.getPlayBackConnection().name.uppercase(),
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
        )
    }
}

@Composable
private fun PlaybackSheetTopBarActions(
    //playbackQueue: PlaybackQueue,
    //onSaveQueueAsPlaylist: Callback,
    actionHandler: AudioActionHandler = LocalAudioActionHandler.current,
) {
    val (expanded, setExpanded) = remember { mutableStateOf(false) }
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
//        if (playbackQueue.isValid) {
//            val (addToPlaylistVisible, setAddToPlaylistVisible) = remember { mutableStateOf(false) }
//            val (addQueueToPlaylistVisible, setAddQueueToPlaylistVisible) = remember { mutableStateOf(false) }
//
//            AddToPlaylistMenu(playbackQueue.currentAudio, addToPlaylistVisible, setAddToPlaylistVisible)
//            AddToPlaylistMenu(playbackQueue.audios, addQueueToPlaylistVisible, setAddQueueToPlaylistVisible)
//
//            AudioDropdownMenu(
//                expanded = expanded,
//                onExpandedChange = setExpanded,
//                actionLabels = currentPlayingMenuActionLabels,
//                extraActionLabels = listOf(AddQueueToPlaylist, SaveQueueAsPlaylist)
//            ) { actionLabel ->
//                val audio = playbackQueue.currentAudio
//                when (val action = AudioItemAction.from(actionLabel, audio)) {
//                    is AudioItemAction.AddToPlaylist -> setAddToPlaylistVisible(true)
//                    else -> {
//                        action.handleExtraActions(actionHandler) {
//                            when (it.actionLabelRes) {
//                                AddQueueToPlaylist -> setAddQueueToPlaylistVisible(true)
//                                SaveQueueAsPlaylist -> onSaveQueueAsPlaylist()
//                            }
//                        }
//                    }
//                }
//            }
    MoreVerticalIcon()
    }
}


@Composable
private fun PlaybackAudioInfo(homePage: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    // val dlItem = audio.audioDownloadItem
    if (homePage != null) {
       // val audiHeader = dlItem.audioHeader(context)
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = AppTheme.specs.padding)
        ) {
            Surface(
                color = MaterialTheme.colors.plainBackgroundColor().copy(alpha = 0.1f),
                shape = CircleShape,
            ) {
                Text(
                    text = homePage,
                    style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
private fun LazyListScope.playbackQueue(
    //playbackQueue: PlaybackQueue,
    scrollToTop: Callback,
    playbackConnection: PlaybackConnection,
) {
//    val lastIndex = playbackQueue.audios.size
//    val firstIndex = (playbackQueue.currentIndex + 1).coerceAtMost(lastIndex)
//    val queue = playbackQueue.audios.subList(firstIndex, lastIndex)
//    itemsIndexed(queue, key = { _, a -> a.primaryKey }) { index, audio ->
//        val realPosition = firstIndex + index
//        AudioRow(
//            audio = audio,
//            observeNowPlayingAudio = false,
//            imageSize = 40.dp,
//            onPlayAudio = {
//                playbackConnection.transportControls?.skipToQueueItem(realPosition.toLong())
//                scrollToTop()
//            },
//            extraActionLabels = listOf(RemoveFromPlaylist),
//            onExtraAction = { playbackConnection.removeByPosition(realPosition) },
//            modifier = Modifier.animateItemPlacement()
//        )
//    }
}


