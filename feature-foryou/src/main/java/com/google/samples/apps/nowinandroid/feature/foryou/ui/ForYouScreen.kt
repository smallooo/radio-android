/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.nowinandroid.feature.foryou

import androidx.annotation.IntRange
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.samples.apps.nowinandroid.components.Pager
import com.google.samples.apps.nowinandroid.components.PagerState
import com.google.samples.apps.nowinandroid.core.ui.LoadingWheel
import com.google.samples.apps.nowinandroid.core.ui.component.NiaGradientBackground
import com.google.samples.apps.nowinandroid.core.ui.component.NiaTopAppBar
import com.google.samples.apps.nowinandroid.feature.foryou.ui.ShimmerList

@Composable
fun ForYouRoute(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    viewModel: CountryViewModel = hiltViewModel(),

) {
    ForYouScreen(state = viewModel.state)

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ForYouScreen(
    state: FoodCategoriesContract.State,
) {
    val tabs = listOf("本地电台", "访问排行", "投票排行","最近更新", "正在播放", "标签", "国家", "语言", "搜索")

    @Composable
    fun AdvanceListContent() {
        var selectedIndex by remember { mutableStateOf(0) }
        val pagerState: PagerState = run {
            remember {
                PagerState(0, 0, tabs.size - 1)
            }
        }

        Column {
            Spacer(modifier = Modifier.height(88.dp))
            ScrollableTabRow(
                selectedTabIndex = selectedIndex,
                edgePadding = 12.dp
            ) {
                tabs.forEachIndexed      { index, title ->
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
                    0 -> ShimmerList(state)
                    1 -> ShimmerList(state)
                    2 -> ShimmerList(state)
                    3 -> ShimmerList(state)
                    4 -> ShimmerList(state)
                    5 -> ShimmerList(state)
                    6 -> LocalRadioList(state)
                    7 -> ShimmerList(state)
                    8 -> ShimmerList(state)
                    9 -> ShimmerList(state)
                    10 -> ShimmerList(state)
                    11 -> ShimmerList(state)
                    12 -> ShimmerList(state)
                    13 -> ShimmerList(state)
                    14 -> ShimmerList(state)
                }
            }
        }
    }

    NiaGradientBackground {
        Scaffold(
            topBar = {
                NiaTopAppBar(
                    titleRes = R.string.app_name,
                    navigationIcon = Icons.Filled.Search,
                    navigationIconContentDescription = stringResource(
                        id = R.string.top_app_bar_navigation_button_content_desc
                    ),
                    actionIcon = Icons.Outlined.Timer,
                    actionIconContentDescription = stringResource(
                        id = R.string.settings_alarm_sleep_timer
                    ),
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier.windowInsetsPadding(
                        WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
                    )
                )
            },
            containerColor = Color.Transparent
        ) { innerPadding ->
            innerPadding
            AdvanceListContent()
        }
    }
}


