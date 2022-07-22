package com.dmhsh.samples.apps.nowinandroid.feature.foryou

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavBackStackEntry
import com.dmhsh.samples.apps.nowinandroid.common.compose.LocalPlaybackConnection
import com.dmhsh.samples.apps.nowinandroid.components.Pager
import com.dmhsh.samples.apps.nowinandroid.components.PagerState
import com.dmhsh.samples.apps.nowinandroid.core.navigation.LocalNavigator
import com.dmhsh.samples.apps.nowinandroid.core.navigation.Navigator
import com.dmhsh.samples.apps.nowinandroid.core.navigation.Screens.LeafScreen
import com.dmhsh.samples.apps.nowinandroid.core.navigation.Screens.QUERY_KEY
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.SettingMenu
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.RadioTab
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.RadioTopAppBar
import com.dmhsh.samples.apps.nowinandroid.core.ui.extensions.isNotNullandNotBlank
import com.dmhsh.samples.apps.nowinandroid.playback.PlaybackConnection
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
fun ForYouScreen(navigation: Navigator = LocalNavigator.current,
                 showMenu: MutableState<Boolean>  = remember { mutableStateOf(false) },
                 playbackConnection: PlaybackConnection = LocalPlaybackConnection.current) {
    var sliderState by remember { mutableStateOf(playbackConnection.timeRemained.toFloat()) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            RadioTopAppBar(
                titleRes = R.string.app_name,
                navigationIcon = Icons.Filled.Search,
                navigationIconContentDescription = stringResource(id = R.string.app_name),
                actionIcon = Icons.Outlined.Timer,
                actionIconContentDescription = stringResource(id = R.string.app_name),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colors.background,
                    titleContentColor = MaterialTheme.colors.onSurface,
                ),
                modifier = Modifier.windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
                ),
                onNavigationClick = {
                    navigation.navigate(LeafScreen.SearchingSheet().createRoute())
                },
                onActionClick = {
                    showMenu.value = !showMenu.value
                   // Log.e("aaa, timeRe", playbackConnection.timeRemained.toInt().toString())
                    sliderState = playbackConnection.timeRemained.toFloat()
                }
            )
        },
    ) { innerPadding ->
        val padding = innerPadding
        AdvanceListContent(showMenu, playbackConnection, sliderState, onValueChange = {
            sliderState = it
        })
    }
}

@Composable
fun getNavArgument(key: String): Any? {
    val owner = LocalViewModelStoreOwner.current
    return if (owner is NavBackStackEntry) owner.arguments?.get(key)
    else null
}

@Composable
fun AdvanceListContent(showMenu: MutableState<Boolean>,
                       playbackConnection: PlaybackConnection,
                       sliderState: Float,
                       context: Context = LocalContext.current,
                       viewModel: SearchListViewModel = hiltViewModel(),
                       onValueChange:(Float) -> Unit,
                       ) {
    var selectedIndex by remember { mutableStateOf(0) }
    var initialQuery = (getNavArgument(QUERY_KEY) ?: "").toString()
    var query by rememberSaveable { mutableStateOf(initialQuery) }



   // var settingString = stringResource(id = R.string.action_ok)


    val tabs = listOf(stringResource(R.string.action_local), stringResource(R.string.action_top_click), stringResource(R.string.action_top_vote), stringResource(R.string.action_changed_lately),
        stringResource(R.string.action_currently_playing), stringResource(R.string.action_tags), stringResource(R.string.action_countries), stringResource(R.string.action_languages), stringResource(R.string.action_search))

    val pagerState1: PagerState = run { remember { PagerState(0, 0, tabs.size - 1) } }

    if(query.isNotNullandNotBlank()){

        selectedIndex = tabs.lastIndex
        pagerState1.currentPage = tabs.lastIndex
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                            pagerState1.currentPage = tabs.indexOf(title)
                        },
                        text = { Text(text = title) }
                    )
                }
            }

            Pager(state = pagerState1, modifier = Modifier.weight(1f)) {
                selectedIndex = pagerState1.currentPage
                when (commingPage) {
                    0 -> LocalRadioList()  //0
                    1 -> TopClickRadios()
                    2 -> TopVoteRadios()
                    3 -> LateUpdateRadios()
                    4 -> NowPlayingRadios()
                    5 -> TagListScreen(onTagSelect = { stationTag ->
                        LaunchSearchScreen(pagerState1, viewModel, "bytag", stationTag.name)
                    }) //0
                    6 -> CountryList(onCountrySelect = { Country ->
                        LaunchSearchScreen(pagerState1, viewModel, "bycountry", Country.name)
                    })
                    7 -> LanguageListScreen(onTagSelect = {
                        LaunchSearchScreen(pagerState1, viewModel, "bylanguage", it.name)
                    })  //0
                    8 -> SearchStationsScreen(
                        query,
                        onButtonSelect = { it ->
                            selectedIndex = it + 5
                            pagerState1.currentPage = it + 5
                            query = ""
                        }
                    )
                }
            }
        }

        if (showMenu.value) {

            SettingMenu(
                sliderState,
                playbackConnection.timeRemained,
                modifier = Modifier.align(Alignment.TopEnd),
                onValueChange = onValueChange,
                onTimerSet = {
                    if(it > 1) {
                       // Toast.makeText(context, settingString, Toast.LENGTH_SHORT).show()
                        playbackConnection.stopPlayInSeconds(it * 60)
                        showMenu.value = false
                    }else{
                        showMenu.value = false
                    }
                }
            )
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



