/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.hdmsh.core_ui_playback.searching

import android.annotation.SuppressLint
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.WindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dmhsh.samples.app.nowinandroid.core.playback.*

import com.google.accompanist.pager.ExperimentalPagerApi
import com.dmhsh.samples.apps.nowinandroid.common.compose.LocalPlaybackConnection
import com.dmhsh.samples.apps.nowinandroid.common.compose.LocalScaffoldState
import com.dmhsh.samples.apps.nowinandroid.core.model.data.Station
import com.dmhsh.samples.apps.nowinandroid.core.navigation.LocalNavigator
import com.dmhsh.samples.apps.nowinandroid.core.navigation.Navigator
import com.dmhsh.samples.apps.nowinandroid.core.ui.adaptiveColor
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.*
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.AnimatedListItem

import com.dmhsh.samples.apps.nowinandroid.core.ui.extensions.Callback
import com.dmhsh.samples.apps.nowinandroid.core.ui.theme.AppTheme
import com.dmhsh.samples.apps.nowinandroid.core.ui.theme.RadioTheme
import com.dmhsh.samples.apps.nowinandroid.playback.PlaybackConnection
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.WindowInsets
import com.hdmsh.common_compose.rememberFlowWithLifecycle
import com.hdmsh.core_ui_playback.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@Composable
fun SearchingSheet(
    navigator: Navigator = LocalNavigator.current,
    viewModel: SearchViewModel = hiltViewModel(),

) {
    val listState = rememberLazyListState()
    val queueListState = rememberLazyListState()
    val coroutine = rememberCoroutineScope()
    val scrollToTop: Callback = { coroutine.launch { listState.animateScrollToItem(0) } }

    RadioTheme(changeSystemBar = false) {
        SearchingSheetContent(
            onClose = { navigator.goBack() },
            scrollToTop = scrollToTop,
            listState = listState,
            queueListState = queueListState,
            viewModel = viewModel){ action -> viewModel.submitAction(action) }
        }
    }

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun SearchingSheetContent(
    onClose: Callback,
    scrollToTop: Callback,
    listState: LazyListState,
    queueListState: LazyListState,
    scaffoldState: ScaffoldState = rememberScaffoldState(snackbarHostState = LocalScaffoldState.current.snackbarHostState),
    playbackConnection: PlaybackConnection = LocalPlaybackConnection.current,
    viewModel: SearchViewModel = hiltViewModel(),
    actioner: (SearchAction) -> Unit
) {
    val nowPlaying by rememberFlowWithLifecycle(playbackConnection.nowPlaying)
    val adaptiveColor by adaptiveColor(nowPlaying.artwork, initial = MaterialTheme.colors.onBackground)
    val viewState by rememberFlowWithLifecycle(viewModel.state)

    LaunchedEffect(playbackConnection) {
        playbackConnection.playbackState
            .filter { it != NONE_PLAYBACK_STATE }
            .collectLatest { if (it.isIdle) onClose() }
    }

    BoxWithConstraints {
        val isWideLayout = isWideLayout()
        Row(Modifier.fillMaxSize()) {
            if (isWideLayout) { }
            Scaffold(
                backgroundColor = Color.Transparent,
                modifier = Modifier
                    .background(adaptiveColor.gradient)
                    .weight(1f),
                scaffoldState = scaffoldState,
                snackbarHost = {
                    DismissableSnackbarHost(it, modifier = Modifier.navigationBarsPadding())
                },
            ) {
                RadioSearchScreen(
                    viewModel = viewModel,
                    onQueryChange = { actioner(SearchAction.QueryChange(it)) },
                    onSearch = { actioner(SearchAction.Search) },
                    onBackendTypeSelect = { actioner(it) },
                    state = viewState,
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RadioSearchScreen(
    viewModel: SearchViewModel,
    state: SearchViewState,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onBackendTypeSelect: (SearchAction.SelectBackendType) -> Unit = {},
    focusManager: FocusManager = LocalFocusManager.current,
    windowInfo: WindowInfo = LocalWindowInfo.current,
    windowInsets: WindowInsets = LocalWindowInsets.current,
) {
    val viewState =viewModel.stateS
    val scrollState = rememberLazyListState(0)
    val surfaceGradient = SpotifyDataProvider.spotifySurfaceGradient(false) //SpotifyDataProvider.spotifySurfaceGradient(isSystemInDarkTheme())
    val initialQuery = "".toString()
    Box(modifier = Modifier
        .fillMaxSize()
        .horizontalGradientBackground(surfaceGradient)) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val hasWindowFocus = windowInfo.isWindowFocused
        val keyboardVisible = windowInsets.ime.isVisible
        val focused by remember { mutableStateOf(false) }
        val searchActive = focused && hasWindowFocus && keyboardVisible
        val triggerSearch = {
            onSearch();
            keyboardController?.hide();
            focusManager.clearFocus() }

       // Spacer(modifier = Modifier.height(280.dp))
        if(scrollState.firstVisibleItemIndex < 2) {
            SearchTitle(typography, scrollState)
        }
        //Column(modifier = Modifier.verticalScroll(scrollState)) {
        LazyColumn(state = scrollState) {
            item {
                Spacer(modifier = Modifier.height(280.dp))
            }

            item {
                    // SpotifySearchBar()
                Column() {
                    SearchInput(
                        initialQuery,
                        onQueryChange,
                        searchActive,
                        focused,
                        state,
                        onBackendTypeSelect,
                        triggerSearch
                    )
                }
            }

                item {
                    Spacer(modifier = Modifier.height(60.dp))
                }

                    if (!viewState.isWaiting) {
                        if (viewState.isLoading) {
                            item { FullScreenLoading() }
                        } else {
                            viewState.localStations.forEachIndexed { index, item ->
                                item {
                                    AnimatedSearchListItem(
                                        surfaceGradient,
                                            viewModel,
                                            station = item,
                                            index,
                                            onImageClick = {},
                                            onPlayClick = {})
                                }
                            }
                        }
                    }
                    //SpotifySearchGrid()
                }
            }
        Spacer(modifier = Modifier.height(200.dp))
    }
       // }

@Composable
private fun SearchTitle(
    typography: Typography,
    scrollState: LazyListState
) {
        Text(
            text = "Search",
            style = typography.h3.copy(fontWeight = FontWeight.ExtraBold),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 160.dp, bottom = 40.dp)
                .fillMaxSize()
                .alpha(1f - scrollState.firstVisibleItemScrollOffset)
        )
}

@Composable
private fun ColumnScope.SearchInput(
    initialQuery: String,
    onQueryChange: (String) -> Unit,
    searchActive: Boolean,
    focused: Boolean,
    state: SearchViewState,
    onBackendTypeSelect: (SearchAction.SelectBackendType) -> Unit,
    triggerSearch: () -> Unit
) {
    var focused1 = focused
    var query by rememberSaveable { mutableStateOf(initialQuery) }
    SearchTextField(value = query, onValueChange = { value ->
        query = value
        onQueryChange(value)
    },
        onSearch = { triggerSearch() },
        hint = if (!searchActive) stringResource(R.string.searchpreference_search) else stringResource(R.string.action_search),
        analyticsPrefix = "search",
        modifier = Modifier
            .padding(horizontal = AppTheme.specs.padding)
            .onFocusChanged { focused1 = it.isFocused })

    var backends = state.filter.backends
    if (backends == SearchFilter.DefaultBackends)
        backends = emptySet()

    val filterVisible = searchActive || query.isNotBlank() || backends.isNotEmpty()
    SearchFilterPanel(visible = filterVisible, backends) { selectAction ->
        onBackendTypeSelect(selectAction)
        triggerSearch()
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ColumnScope.SearchFilterPanel(
    visible: Boolean,
    selectedItems: Set<DatmusicSearchParams.BackendType>,
    onBackendTypeSelect: (SearchAction.SelectBackendType) -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = expandIn(expandFrom = Alignment.TopCenter) + fadeIn(),
        exit = shrinkOut(shrinkTowards = Alignment.BottomCenter) + fadeOut()
    ) {
        ChipsRow(
            items = DatmusicSearchParams.BackendType.values().take(4).toList(),
            selectedItems = selectedItems,
            onItemSelect = { selected, item ->
                onBackendTypeSelect(SearchAction.SelectBackendType(selected, item))
            },
            labelMapper = {
                stringResource(
                    when (it) {
                        DatmusicSearchParams.BackendType.Stations -> R.string.nav_item_stations
                        DatmusicSearchParams.BackendType.Country -> R.string.detail_country
                        DatmusicSearchParams.BackendType.Languages -> R.string.action_languages
                        DatmusicSearchParams.BackendType.Tags -> R.string.detail_tags
                        DatmusicSearchParams.BackendType.FLACS -> R.string.app_id
                        else -> {
                            R.string.action_search
                        }
                    }
                )
            }
        )
    }
}

fun Modifier.gradientBackground(
    colors: List<Color>,
    brushProvider: (List<Color>, Size) -> Brush
): Modifier = composed {
    var size by remember { mutableStateOf(Size.Zero) }
    val gradient = remember(colors, size) { brushProvider(colors, size) }
    drawWithContent {
        size = this.size
        drawRect(brush = gradient)
        drawContent()
    }
}

fun Modifier.horizontalGradientBackground(
    colors: List<Color>
) = gradientBackground(colors) { gradientColors, size ->
    Brush.horizontalGradient(
        colors = gradientColors,
        startX = 0f,
        endX = size.width
    )
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AnimatedSearchListItem(
    surfaceGradient:  List<Color> ,
    viewModel: ViewModel,
    station: Station,
    itemIndex: Int,
    onImageClick: (station: Station) -> Unit,
    onPlayClick: (station: Station) -> Unit)
{
    val playbackConnection: PlaybackConnection = LocalPlaybackConnection.current
    var expanded by remember { mutableStateOf(false) }
    var favorite by remember { mutableStateOf(station.favorited) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 8.dp)
            .clickable {
                playbackConnection.playAudio(station)
                onPlayClick(station)

            },
    ) {
        CoverImage(
            data = station.favicon,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(55.dp)
                .padding(4.dp)
                .clickable {
                    // onImageClick(station)
                    // favorite = !favorite
                }
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .weight(1f)
        ) {
            androidx.compose.material3.Text(
                text = station.name,
                style = typography.body1,
                color = MaterialTheme.colors.onSurface
            )

            androidx.compose.material3.Text(
                text = station.bitrate + "kbps",
                style = typography.body1,
                maxLines = 1,
                color = MaterialTheme.colors.onSurface,
                overflow = TextOverflow.Ellipsis
            )
        }
//        androidx.compose.material3.Icon(
//            imageVector = Icons.Default.ArrowDropUp,
//            contentDescription = null,
//            tint = Color.LightGray,
//            modifier = Modifier
//                .padding(8.dp)
//                .size(32.dp)
//                .background(
//                    if(playbackConnection.playingStation.value.stationuuid == station.stationuuid) Color.Red else Color.LightGray)
//                .clickable {  }
//        )

        if(station.stationuuid == playbackConnection.playingStation.value.stationuuid) {
            if(!playbackConnection.isConnected.value){
                FullScreenLoading()
            }
            PlaybackPlayPause(playbackConnection.playbackState.value, onPlayPause = {})
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



