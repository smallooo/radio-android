/*
 * Copyright 2021 The Android Open Source Project
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

package com.dmhsh.samples.apps.nowinandroid.feature.interests

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.dmhsh.samples.apps.nowinandroid.core.model.data.FollowableStation
import com.dmhsh.samples.apps.nowinandroid.core.model.data.Station
import com.dmhsh.samples.apps.nowinandroid.core.ui.LoadingWheel
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.*
import com.dmhsh.samples.apps.nowinandroid.feature.interests.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun <T> rememberFlowWithLifecycle(
    flow: Flow<T>,
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED
): Flow<T> = remember(flow, lifecycle) {
    flow.flowWithLifecycle(
        lifecycle = lifecycle,
        minActiveState = minActiveState
    )
}

@Composable
fun InterestsRoute(
    modifier: Modifier = Modifier,
    navigateToAuthor: (String) -> Unit = {},
    navigateToTopic: (String) -> Unit = {},
    favoriteStationstViewModel: FavoriteStationstViewModel = hiltViewModel()
) {

    val uiState1 by favoriteStationstViewModel.favoriteStationsState.collectAsState()

    InterestsScreen(
        uiState1 = uiState1,
        navigateToAuthor = navigateToAuthor,
        navigateToTopic = navigateToTopic,
        modifier = modifier
    )
}

@Composable
fun InterestsScreen(
    uiState1: StationsUiState,
    navigateToAuthor: (String) -> Unit,
    navigateToTopic: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            // TODO: Replace with windowInsetsTopHeight after
            //       https://issuetracker.google.com/issues/230383055
            Modifier.windowInsetsPadding(
                WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
            )
        )

        NiaTopAppBar(
            titleRes = R.string.interests,
            navigationIcon = Icons.Filled.Search,
            navigationIconContentDescription = stringResource(
                id = R.string.top_app_bar_navigation_button_content_desc
            ),
            actionIcon = Icons.Filled.MoreVert,
            actionIconContentDescription = stringResource(
                id = R.string.top_app_bar_navigation_button_content_desc
            )
        )

        
        when (uiState1) {
            StationsUiState.Loading ->
                LoadingWheel(
                    modifier = modifier,
                    contentDesc = stringResource(id = R.string.interests_loading),
                )
            is StationsUiState.Stations ->
               // Text(favoriteState.stations.get(0).station.name.toString())
                RadioItemFavorite(listOf((uiState1 as StationsUiState.Stations).stations), onImageClick = {})
               // RadioItem(listOf(favoriteState .stations))
            is StationsUiState.Empty -> InterestsEmptyScreen()
        }
    }
}


@Composable
private fun InterestsEmptyScreen() {
    Text(text = stringResource(id = R.string.interests_empty_header))
}
