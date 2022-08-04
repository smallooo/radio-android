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

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.dmhsh.samples.apps.nowinandroid.common.compose.LocalPlaybackConnection
import com.dmhsh.samples.apps.nowinandroid.core.ui.LoadingWheel
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.*
import com.dmhsh.samples.apps.nowinandroid.playback.PlaybackConnection
import kotlinx.coroutines.flow.Flow

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
    favoriteStationstViewModel: FavoriteStationstViewModel = hiltViewModel(),
    playbackConnection: PlaybackConnection = LocalPlaybackConnection.current,
    ) {
    val uiState1 by favoriteStationstViewModel.favoriteStationsState.collectAsState()
    val localStations: LocalStationsUiState by favoriteStationstViewModel.localStationState.collectAsState()

    InterestsScreen(
        favoriteStationstViewModel = favoriteStationstViewModel,
        localStationsState = uiState1,
        localStations
    )
}

@Composable
fun InterestsScreen(
    favoriteStationstViewModel: FavoriteStationstViewModel,
    localStationsState: StationsUiState,
    localStations :LocalStationsUiState
) {
    Scaffold(
        topBar = {
            SimpleTopAppBar(
                titleRes = R.string.interests,
                navigationIcon = Icons.Filled.Search,
                navigationIconContentDescription = stringResource(id = R.string.top_app_bar_navigation_button_content_desc),
                actionIcon = Icons.Filled.MoreVert,
                actionIconContentDescription = stringResource(id = R.string.top_app_bar_navigation_button_content_desc),
                modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top)))
        }
    ) { padding ->
        val pad = padding
        when (localStationsState) {
            is StationsUiState.Loading ->
                LoadingWheel(modifier = Modifier, contentDesc = stringResource(id = R.string.interests_loading))
            is StationsUiState.Stations ->
                if(localStationsState.stations.isEmpty()){
                    if( localStations is LocalStationsUiState.Stations){
                        DatingHomeScreen(localStations .stations1)

                    }
                }else {
                    RadioItemFavorite(favoriteStationstViewModel, listOf(localStationsState.stations),
                        onImageClick = {
                            favoriteStationstViewModel.setFavoritedStation(it)
                        })
                }
            is StationsUiState.Empty -> InterestsEmptyScreen()

        }
    }
}

@Composable
private fun InterestsEmptyScreen() {
    Text(text = stringResource(id = R.string.interests_empty_header))
}