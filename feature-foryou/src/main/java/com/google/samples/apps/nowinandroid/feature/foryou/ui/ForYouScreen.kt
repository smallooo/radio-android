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
import com.google.samples.apps.nowinandroid.core.navigation.LocalNavigator

import com.google.samples.apps.nowinandroid.core.navigation.Navigator
import com.google.samples.apps.nowinandroid.core.navigation.Screens.LeafScreen
import com.google.samples.apps.nowinandroid.core.ui.component.NiaGradientBackground
import com.google.samples.apps.nowinandroid.core.ui.component.NiaTopAppBar
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
    val tabs = listOf(stringResource(com.google.samples.apps.nowinandroid.feature.foryou.R.string.for_you),
        stringResource(R.string.nav_item_save_playlist),
        "投票排行",
        "最近更新",
        "正在播放",
        "标签",
        "国家",
        "语言",
        "搜索")
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
                0 -> LocalRadioList(PageType.bycountry, "")
                1 -> TopVisitRadios(PageType.bycountry, "")
                2 -> TopVoteRadios(PageType.bycountry, "")
                3 -> LateUpdateRadios(PageType.bycountry, "")
                4 -> NowPlayingRadios(PageType.bycountry, "")
                5 -> TagListScreen(onTagSelect = {stationTag ->
                    GlobalScope.launch(Dispatchers.IO) {
                        selectedIndex = 8
                        pagerState.currentPage = selectedIndex
                        viewModel.upDateSearch("bytag", stationTag.name)
                    }
                })
                6 -> CountryList(onCountrySelect = {Country ->
                    selectedIndex = 8
                    pagerState.currentPage = selectedIndex
                    GlobalScope.launch(Dispatchers.IO) {
                        selectedIndex = 8
                        pagerState.currentPage = selectedIndex
                        viewModel.upDateSearch("bycountry", Country.name)
                    }
                })
                7 -> LanguageListScreen(onTagSelect = {
                    GlobalScope.launch(Dispatchers.IO) {
                        selectedIndex = 8
                        pagerState.currentPage = selectedIndex
                        viewModel.upDateSearch("bylanguage", it.name)
                    }
                })
                8 -> SearchStationsScreen()
            }
        }
    }
}



