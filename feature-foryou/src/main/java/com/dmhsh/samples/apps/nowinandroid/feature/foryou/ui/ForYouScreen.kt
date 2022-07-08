package com.dmhsh.samples.apps.nowinandroid.feature.foryou

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dmhsh.samples.apps.nowinandroid.components.Pager
import com.dmhsh.samples.apps.nowinandroid.components.PagerState
import com.dmhsh.samples.apps.nowinandroid.core.navigation.LocalNavigator
import com.dmhsh.samples.apps.nowinandroid.core.navigation.Navigator
import com.dmhsh.samples.apps.nowinandroid.core.navigation.Screens.LeafScreen
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.RadioTab
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.RadioTopAppBar
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
fun ForYouScreen(navigation: Navigator = LocalNavigator.current) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            RadioTopAppBar(
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
                    containerColor = MaterialTheme.colors.background,
                    titleContentColor = MaterialTheme.colors.onSurface,
                ),
                modifier = Modifier.windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
                ),
                onNavigationClick = {
                    navigation.navigate(LeafScreen.PlaybackSheet().createRoute())
                }
            )
        },
    ) { innerPadding ->
        val padding = innerPadding
        AdvanceListContent()
    }
}

@Composable
fun AdvanceListContent(viewModel: SearchListViewModel = hiltViewModel()) {
    var selectedIndex by remember { mutableStateOf(0) }
    val tabs = listOf(
        stringResource(R.string.action_local),
        stringResource(R.string.action_top_click),
        stringResource(R.string.action_top_vote),
        stringResource(R.string.action_changed_lately),
        stringResource(R.string.action_currently_playing),
        stringResource(R.string.action_tags),
        stringResource(R.string.action_countries),
        stringResource(R.string.action_languages),
        stringResource(R.string.action_search)
    )
    val pagerState: PagerState = run { remember { PagerState(0, 0, tabs.size - 1) } }

    Column {
        ScrollableTabRow(
            selectedTabIndex = selectedIndex,
            edgePadding = 0.dp,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    color = MaterialTheme.colors.secondary,
                    height = 3.dp,
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedIndex])
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                RadioTab(
                    selected = index == selectedIndex,
                    onClick = {
                        selectedIndex = tabs.indexOf(title)
                        pagerState.currentPage = tabs.indexOf(title)
                    },
                    text = { Text(text = title) }
                )
            }
        }

        Pager(state = pagerState, modifier = Modifier.weight(1f)) {
            selectedIndex = pagerState.currentPage
            when (commingPage) {
                0 -> LocalRadioList()  //0
                1 -> TopClickRadios()
                2 -> TopVoteRadios()
                3 -> LateUpdateRadios()
                4 -> NowPlayingRadios()
                5 -> TagListScreen(onTagSelect = { stationTag ->
                    LaunchSearchScreen(pagerState, viewModel, "bytag", stationTag.name) }) //0
                6 -> CountryList(onCountrySelect = { Country ->
                    LaunchSearchScreen(pagerState, viewModel, "bycountry", Country.name) })
                7 -> LanguageListScreen(onTagSelect = {
                    LaunchSearchScreen(pagerState, viewModel, "bylanguage", it.name) })  //0
                8 -> SearchStationsScreen()
            }
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
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



