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


@Composable
fun ForYouRoute(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    viewModel: FoodCategoriesViewModel = hiltViewModel()
) {

    ForYouScreen(
        //onAuthorCheckedChanged = viewModel::updateAuthorSelection,
    state = viewModel.state
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ForYouScreen(
    //onAuthorCheckedChanged: (String, Boolean) -> Unit,
    state: FoodCategoriesContract.State,
) {
    val tabs = listOf("Shimmers", "Animated Lists", "Swipeable Lists","Shimmers", "Animated Lists", "Swipeable Lists")

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

            if(state.isLoading){
                Text("Loading")
            }else if(state.categories.size > 0){

                Text(state.categories.get(0).description)
            }
            ScrollableTabRow(
                selectedTabIndex = selectedIndex,
                edgePadding = 12.dp
            ) {
                tabs.forEachIndexed { index, title ->
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
                    0 -> ShimmerList()
                    1 -> ShimmerList()
                    2 -> ShimmerList()
                    3 -> ShimmerList()
                    4 -> ShimmerList()
                    5 -> ShimmerList()
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

/**
 * An extension on [LazyListScope] defining the feed portion of the for you screen.
 * Depending on the [feedState], this might emit no items.
 *
 * @param showLoadingUIIfLoading if true, show a visual indication of loading if the
 * [feedState] is loading. This is controllable to permit du-duplicating loading
 * states.
 */
private fun LazyListScope.Feed(
    feedState: ForYouFeedUiState,
    showLoadingUIIfLoading: Boolean,
    @IntRange(from = 1) numberOfColumns: Int,
    onNewsResourcesCheckedChanged: (String, Boolean) -> Unit
) {
    when (feedState) {
        ForYouFeedUiState.Loading -> {
            if (showLoadingUIIfLoading) {
                item {
                    LoadingWheel(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(),
                        contentDesc = stringResource(id = R.string.for_you_loading),
                    )
                }
            }
        }
        is ForYouFeedUiState.Success -> {
            items(
                feedState.feed.chunked(numberOfColumns)
            ) { saveableNewsResources ->
                Row(
                    modifier = Modifier.padding(
                        top = 32.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    repeat(numberOfColumns) { index ->
                        Box(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "hello")
                        }
                    }
                }
            }
        }
    }
}

