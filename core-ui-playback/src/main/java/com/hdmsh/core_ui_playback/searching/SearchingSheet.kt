/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.hdmsh.core_ui_playback.searching

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.WindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

import com.google.accompanist.pager.ExperimentalPagerApi
import com.dmhsh.samples.app.nowinandroid.core.playback.NONE_PLAYBACK_STATE
import com.dmhsh.samples.app.nowinandroid.core.playback.artwork
import com.dmhsh.samples.app.nowinandroid.core.playback.isIdle
import com.dmhsh.samples.apps.nowinandroid.common.compose.LocalPlaybackConnection
import com.dmhsh.samples.apps.nowinandroid.common.compose.LocalScaffoldState
import com.dmhsh.samples.apps.nowinandroid.core.navigation.LocalNavigator
import com.dmhsh.samples.apps.nowinandroid.core.navigation.Navigator
import com.dmhsh.samples.apps.nowinandroid.core.ui.adaptiveColor
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.DismissableSnackbarHost

import com.dmhsh.samples.apps.nowinandroid.core.ui.component.isWideLayout
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
            viewModel = viewModel){
                action -> viewModel.submitAction(action)
        }
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
                    onQueryChange = {
                        Log.e("aaa", "onQueryChange")
                        actioner(SearchAction.QueryChange(it)) },
                    onSearch = {
                        Log.e("aaa", "onSearch")
                        actioner(SearchAction.Search) },
                    onBackendTypeSelect = {
                        Log.e("aaa", "onBackendTypeSelect")
                        actioner(it) },
                    state = viewState,
                )
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RadioSearchScreen(
    state: SearchViewState,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onBackendTypeSelect: (SearchAction.SelectBackendType) -> Unit = {},
    focusManager: FocusManager = LocalFocusManager.current,
    windowInfo: WindowInfo = LocalWindowInfo.current,
    windowInsets: WindowInsets = LocalWindowInsets.current,
) {
    val scrollState = rememberScrollState(0)
    val surfaceGradient = SpotifyDataProvider.spotifySurfaceGradient(isSystemInDarkTheme())
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

        SearchTitle(typography, scrollState)

        Column(modifier = Modifier.verticalScroll(scrollState)) {
            Spacer(modifier = Modifier.height(180.dp))
            Column(modifier = Modifier.horizontalGradientBackground(surfaceGradient)) {
                // SpotifySearchBar()
                SearchInput(
                    initialQuery,
                    onQueryChange,
                    searchActive,
                    focused,
                    state,
                    onBackendTypeSelect,
                    triggerSearch
                )


                Spacer(modifier = Modifier.height(60.dp))

                SearchList(
                    viewModel = viewModel(),
                    "1"
                )



                SpotifySearchGrid()
            }
            Spacer(modifier = Modifier.height(200.dp))
        }
    }
}

@Composable
private fun SearchTitle(
    typography: Typography,
    scrollState: ScrollState
) {
    Text(
        text = "Search",
        style = typography.h3.copy(fontWeight = FontWeight.ExtraBold),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(top = 80.dp, bottom = 40.dp)
            .fillMaxSize()
            .alpha(1f - scrollState.value / 200)
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
        hint = if (!searchActive) stringResource(R.string.app_id) else stringResource(R.string.action_search),
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
            items = DatmusicSearchParams.BackendType.values().toList(),
            selectedItems = selectedItems,
            onItemSelect = { selected, item ->
                onBackendTypeSelect(SearchAction.SelectBackendType(selected, item))
            },
            labelMapper = {
                stringResource(
                    when (it) {
                        DatmusicSearchParams.BackendType.AUDIOS -> R.string.action_search
                        DatmusicSearchParams.BackendType.FLACS -> R.string.app_id
                        else -> {
                            R.string.app_id
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



