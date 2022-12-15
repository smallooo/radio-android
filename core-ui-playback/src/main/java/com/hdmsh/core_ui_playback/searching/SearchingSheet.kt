/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.hdmsh.core_ui_playback.searching

import android.annotation.SuppressLint
import android.support.v4.media.session.PlaybackStateCompat
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollBy
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
import com.dmhsh.samples.app.nowinandroid.core.playback.*
import com.dmhsh.samples.apps.nowinandroid.common.compose.LocalPlaybackConnection
import com.dmhsh.samples.apps.nowinandroid.common.compose.LocalScaffoldState
import com.dmhsh.samples.apps.nowinandroid.core.model.data.Station
import com.dmhsh.samples.apps.nowinandroid.core.navigation.LocalNavigator
import com.dmhsh.samples.apps.nowinandroid.core.navigation.Navigator
import com.dmhsh.samples.apps.nowinandroid.core.ui.adaptiveColor
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.*
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
    val listState = rememberLazyListState(0)
    val coroutine = rememberCoroutineScope()
    val scrollToTop: Callback = {
        coroutine.launch {
            listState.scrollBy(-6999f)
        }
    }

    RadioTheme(changeSystemBar = false) {
        SearchingSheetContent(
            onClose = { navigator.goBack() },
            scrollToTop = scrollToTop,
            listState = listState,
            viewModel = viewModel
        ) { action -> viewModel.submitAction(action) }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
internal fun SearchingSheetContent(
    onClose: Callback,
    scrollToTop: () -> Unit,
    listState: LazyListState,
    scaffoldState: ScaffoldState = rememberScaffoldState(snackbarHostState = LocalScaffoldState.current.snackbarHostState),
    playbackConnection: PlaybackConnection = LocalPlaybackConnection.current,
    viewModel: SearchViewModel = hiltViewModel(),
    actioner: (SearchAction) -> Unit
) {
    val nowPlaying by rememberFlowWithLifecycle(playbackConnection.nowPlaying)
    val adaptiveColor by adaptiveColor(nowPlaying.artwork, initial = MaterialTheme.colors.onBackground)
    val viewState by rememberFlowWithLifecycle(viewModel.state)
    var query by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(playbackConnection) {
        playbackConnection.playbackState
            .filter { it != NONE_PLAYBACK_STATE }
            .collectLatest { if (it.isIdle) onClose() }
    }

    BoxWithConstraints {
        Row(Modifier.fillMaxSize()) {
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
                    query = query,
                    viewModel = viewModel,
                    listState = listState,
                    onQueryChange = {
                         query = it
                        actioner(SearchAction.QueryChange(it)) },
                    onSearch = { actioner(SearchAction.Search) },
                    onBackendTypeSelect = { actioner(it) },
                    state = viewState,
                    onRecommend = {
                        scrollToTop()
                        query = it
                        actioner(SearchAction.QueryChange(it))
                        actioner(SearchAction.Search)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RadioSearchScreen(
    query:String,
    viewModel: SearchViewModel,
    listState : LazyListState,
    state: SearchViewState,
    onQueryChange: (String) -> Unit,
    onRecommend: (String) -> Unit,
    onSearch: () -> Unit,
    onBackendTypeSelect: (SearchAction.SelectBackendType) -> Unit = {},
    focusManager: FocusManager = LocalFocusManager.current,
    windowInfo: WindowInfo = LocalWindowInfo.current,
    windowInsets: WindowInsets = LocalWindowInsets.current,

) {
    val viewState by remember { mutableStateOf(viewModel.stateS)}

    val surfaceGradient by rememberSaveable {mutableStateOf(SpotifyDataProvider.spotifySurfaceGradient(false))}

    Box(
        modifier = Modifier
            .fillMaxSize()
            .horizontalGradientBackground(surfaceGradient)
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val hasWindowFocus by rememberSaveable{ mutableStateOf(windowInfo.isWindowFocused)}
        val keyboardVisible by rememberSaveable{ mutableStateOf( windowInsets.ime.isVisible )}
        val focused by remember { mutableStateOf(false) }
        val searchActive by rememberSaveable{ mutableStateOf( focused && hasWindowFocus && keyboardVisible)}
        val triggerSearch = {
            onSearch();
            keyboardController?.hide();
            focusManager.clearFocus()
        }

        if(listState.firstVisibleItemIndex < 1) {
            SearchTitle(typography, listState)
        }

        LazyColumn(state = listState) {
            item { Spacer(modifier = Modifier.height(260.dp)) }

            item { Column { SearchInput(query,onQueryChange, searchActive, focused, state, onBackendTypeSelect, triggerSearch) } }

            item { Spacer(modifier = Modifier.height(60.dp)) }

            if (!viewState.isWaiting) {
                if (viewState.isLoading) {
                    item { FullScreenLoading() }
                } else {
                    viewState.localStations.forEachIndexed { index, item ->
                        item { AnimatedSearchListItem(station = item, onPlayClick = { viewModel.setPlayHistory(station = it) }) }
                    }
                }
            }

            item {
                if(!viewState.isLoading) {
                    SpotifySearchGrid(onSearchSelect = { content -> onRecommend(content) })
                    Spacer(modifier = Modifier.height(120.dp))
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(200.dp))
}

@Composable
private fun SearchTitle(
    typography: Typography,
    scrollState: LazyListState
) {
    Text(
        text = "Search",
        style = typography.h3.copy(fontWeight = FontWeight.ExtraBold),
        color = MaterialTheme.colors.onSurface,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(top = 130.dp, bottom = 40.dp)
            .fillMaxSize()
            .alpha(1f - scrollState.firstVisibleItemScrollOffset / 200)
    )
}

@Composable
private fun ColumnScope.SearchInput(
    query: String,
    onQueryChange: (String) -> Unit,
    searchActive: Boolean,
    focused: Boolean,
    state: SearchViewState,
    onBackendTypeSelect: (SearchAction.SelectBackendType) -> Unit,
    triggerSearch: () -> Unit
) {
    // var focused1 by remember { mutableStateOf(focused)}

    SearchTextField(value = query, onValueChange = { value ->
        onQueryChange(value)
    },
        onSearch = { triggerSearch() },
        hint = if (!searchActive) stringResource(R.string.searchpreference_search) else stringResource(
            R.string.action_search
        ),
        analyticsPrefix = "search",
        modifier = Modifier
            .padding(horizontal = AppTheme.specs.padding)
            .onFocusChanged {
           //     focused1 = it.isFocused
            })

    var backends = state.filter.backends
    if (backends == SearchFilter.DefaultBackends) backends = emptySet()

    val filterVisible = searchActive || query.isNotBlank() || backends.isNotEmpty()
    SearchFilterPanel(visible = filterVisible, backends) { selectAction ->
        onBackendTypeSelect(selectAction)
        triggerSearch()
    }
}

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
            items = DatmusicSearchParams.BackendType.values().take(0).toList(),
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
                        else -> { R.string.action_search }
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
    station: Station,
    onPlayClick: (station: Station) -> Unit
) {
    val playbackConnection: PlaybackConnection = LocalPlaybackConnection.current
    val playbackState by rememberFlowWithLifecycle(playbackConnection.playbackState)
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
                .clickable {}
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

        if (station.stationuuid == playbackConnection.playingStation.value.stationuuid) {
            PlaybackPlayPause(playbackState, onPlayPause = {
                playbackConnection.mediaController?.playPause()
            })
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
        modifier = Modifier.weight(0.15f)
    ) {

        if(playbackState.isBuffering){
            ProgressIndicator()
        }else {
            Icon(
                imageVector = when {
                    playbackState.isError -> Icons.Filled.ErrorOutline
                    playbackState.isBuffering -> Icons.Filled.AcUnit
                    playbackState.isPlaying -> Icons.Filled.Pause
                    playbackState.isPlayEnabled -> Icons.Filled.PlayArrow
                    else -> Icons.Filled.HourglassBottom
                },
                modifier = Modifier.size(size),
                contentDescription = null
            )
        }
    }
}



