/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.hdmsh.core_ui_playback

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
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
import com.dmhsh.samples.apps.nowinandroid.core.model.data.StationsTag
import com.dmhsh.samples.apps.nowinandroid.core.navigation.LocalNavigator
import com.dmhsh.samples.apps.nowinandroid.core.navigation.Navigator
import com.dmhsh.samples.apps.nowinandroid.core.navigation.Screens.LeafScreen
import com.dmhsh.samples.apps.nowinandroid.core.ui.ADAPTIVE_COLOR_ANIMATION
import com.dmhsh.samples.apps.nowinandroid.core.ui.adaptiveColor
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.*

import com.dmhsh.samples.apps.nowinandroid.core.ui.extensions.Callback
import com.dmhsh.samples.apps.nowinandroid.core.ui.media.radioStations.AudioActionHandler
import com.dmhsh.samples.apps.nowinandroid.core.ui.media.radioStations.LocalAudioActionHandler
import com.dmhsh.samples.apps.nowinandroid.core.ui.media.radioStations.audioActionHandler
import com.dmhsh.samples.apps.nowinandroid.core.ui.theme.*
import com.dmhsh.samples.apps.nowinandroid.playback.PlaybackConnection
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.pager.rememberPagerState
import com.hdmsh.common_compose.rememberFlowWithLifecycle
import com.hdmsh.core_ui_playback.components.PlaybackArtworkPagerWithNowPlayingAndControls
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@Composable
fun PlaybackSheet(
    // override local theme color palette because we want simple colors for menus n' stuff
    sheetTheme: ThemeState = LocalThemeState.current.copy(colorPalettePreference = ColorPalettePreference.Black),
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
        RadioTheme(theme = sheetTheme, changeSystemBar = false) {
            PlaybackSheetContent(
                onClose = { navigator.goBack() },
                scrollToTop = scrollToTop,
                listState = listState,
                queueListState = queueListState,
                onTagSelect = {
                     navigator.navigate(LeafScreen.Search.buildRoute(it))
                }
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
    onTagSelect:(stationsTag: String) -> Unit
) {
    val playbackState by rememberFlowWithLifecycle(playbackConnection.playbackState)
    val nowPlaying by rememberFlowWithLifecycle(playbackConnection.nowPlaying)
    val pagerState = rememberPagerState(0)

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

    val contentPadding = rememberInsetsPaddingValues(
        insets = LocalWindowInsets.current.systemBars,
        applyTop = true,
        applyBottom = true,
    )

    BoxWithConstraints {
        val isWideLayout = isWideLayout()
        val maxWidth = maxWidth
        Row(Modifier.fillMaxSize()) {
            if (isWideLayout) {
            }

            Scaffold(
                backgroundColor = Color.Transparent,
                modifier = Modifier
                    .background(adaptiveColor.gradient)
                    .weight(1f),
                scaffoldState = scaffoldState,
                snackbarHost = {
                    DismissableSnackbarHost(
                        it,
                        modifier = Modifier.navigationBarsPadding()
                    )
                },
            ) {
                LazyColumn(
                    state = listState,
                    contentPadding = contentPadding,
                ) {
                    item {
                        PlaybackSheetTopBar(
                            viewModel,
                            onClose = onClose,
                            onTitleClick = {},
                        )
                        Spacer(Modifier.height(AppTheme.specs.paddingTiny))
                    }


                    item {
                        PlaybackArtworkPagerWithNowPlayingAndControls(
                            nowPlaying = nowPlaying,
                            playbackState = playbackState,
                            pagerState = pagerState,
                            contentColor = contentColor,
                            //viewModel = viewModel,
                            modifier = Modifier.fillParentMaxHeight(0.8f),
                        )
                    }

                    if (viewModel.getPlayBackConnection().tags.length > 0)
//                        viewModel.getPlayBackConnection().tags.split(",").forEach {
//                            item {
                        item {
                            LazyRow() {
                                items(viewModel.getPlayBackConnection().tags.split(",")) {
                                    PlaybackAudioInfo(
                                        it,
                                        onTagSelect = onTagSelect
                                    )
                                }
                            }
                        }
//                            }
//                        }

                    if (!isWideLayout) {
                        // playbackQueueLabel()
                    }
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
            IconButton(onClick = onClose) {
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
        MoreVerticalIcon()
    }
}

@Composable
private fun PlaybackAudioInfo(tags:String, modifier: Modifier = Modifier, onTagSelect:(stationsTag: String) -> Unit) {
    val context = LocalContext.current
    // val dlItem = audio.audioDownloadItem
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(
                start = AppTheme.specs.paddingSmall,
                end = AppTheme.specs.paddingSmall,
                bottom = AppTheme.specs.padding
            )
    ) {
            Surface(
                color = MaterialTheme.colors.plainBackgroundColor().copy(alpha = 0.1f),
                shape = CircleShape,
            ) {
                Text(
                    text = tags,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 3.dp)
                        .clickable {
                            onTagSelect(tags)
                        }
                )
            }
    }
}




