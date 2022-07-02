package com.google.samples.apps.nowinandroid.feature.foryou


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import com.google.samples.apps.nowinandroid.components.Pager
import com.google.samples.apps.nowinandroid.components.PagerState
import com.google.samples.apps.nowinandroid.core.datastore.PreferencesStore
import com.google.samples.apps.nowinandroid.core.model.data.StationsTag
import com.google.samples.apps.nowinandroid.core.navigation.LocalNavigator

import com.google.samples.apps.nowinandroid.core.navigation.Navigator
import com.google.samples.apps.nowinandroid.core.navigation.Screens.LeafScreen
import com.google.samples.apps.nowinandroid.core.ui.component.NiaGradientBackground
import com.google.samples.apps.nowinandroid.core.ui.component.NiaTopAppBar
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tm.alashow.i18n.R

@Composable
fun ForYouRoute() {
    ForYouScreen()
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ForYouScreen(
    navigation: Navigator = LocalNavigator.current
) {
    NiaGradientBackground {
        Scaffold(
            topBar = {
                NiaTopAppBar(
                    titleRes = R.string.app_name,
                    navigationIcon = Icons.Filled.Search,
                    navigationIconContentDescription = stringResource(
                        id = R.string.app_name
                    ),
                    actionIcon = Icons.Outlined.Timer,
                    actionIconContentDescription = stringResource(
                        id = R.string.app_name
                    ),
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier.windowInsetsPadding(
                        WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
                    ),
                    onNavigationClick = {
                        navigation.navigate(LeafScreen.PlaybackSheet().createRoute())
                    }
                )
            },
            containerColor = Color.Transparent
        ) { innerPadding ->
            val padding = innerPadding
            AdvanceListContent()
        }
    }
}

@Composable
fun AdvanceListContent(viewModel: SearchListViewModel = hiltViewModel()) {
    var selectedIndex by remember { mutableStateOf(0) }
    val tabs = listOf(stringResource(R.string.action_local),
        stringResource(R.string.action_top_click),
        stringResource(R.string.action_top_vote),
        stringResource(R.string.action_changed_lately),
        stringResource(R.string.action_currently_playing),
        stringResource(R.string.action_tags),
        stringResource(R.string.action_countries),
        stringResource(R.string.action_languages),
        stringResource(R.string.action_search))
    val pagerState: PagerState = run {
        remember { PagerState(0, 0, tabs.size - 1) }
    }

    Column {
        Spacer(modifier = Modifier.height(88.dp))
        ScrollableTabRow(
            selectedTabIndex = selectedIndex,
            edgePadding = 12.dp
        ) {
            tabs.forEachIndexed{ index, title ->
                Tab(
                    selected = index == selectedIndex,
                    onClick = {
                        selectedIndex = tabs.indexOf(title)
                        pagerState.currentPage = tabs.indexOf(title)
                    },
                    text = { Text(title) }
                )
            }
        }
        Pager(state = pagerState, modifier = Modifier.weight(1f)) {
            selectedIndex = pagerState.currentPage
            when (commingPage) {
                0 -> LocalRadioList()
                1 -> TopVisitRadios()
                2 -> TopVoteRadios()
                3 -> LateUpdateRadios()
                4 -> NowPlayingRadios()
                5 -> TagListScreen(onTagSelect = {stationTag -> LaunchSearchScreen(pagerState, viewModel,"bytag", stationTag.name) })
                6 -> CountryList(onCountrySelect = {Country -> LaunchSearchScreen( pagerState, viewModel,"bycountry", Country.name) })
                7 -> LanguageListScreen(onTagSelect = { LaunchSearchScreen(pagerState, viewModel,"bylanguage", it.name) })
                8 -> SearchStationsScreen()
            }
        }
    }
}

private fun LaunchSearchScreen(
    pagerState: PagerState,
    viewModel: SearchListViewModel,
    searchType: String,
    param: String
) {
    GlobalScope.launch(Dispatchers.IO) {
        pagerState.currentPage = 8
        viewModel.upDateSearch(searchType, param)
    }
}



