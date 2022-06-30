package com.dmhsh.feature_history

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.samples.apps.nowinandroid.core.ui.LoadingWheel
import com.google.samples.apps.nowinandroid.core.ui.component.NiaTopAppBar
import com.google.samples.apps.nowinandroid.core.ui.component.RadioItemFavorite

@Composable
fun HistoryRoute(
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {

    val playHistory = viewModel.playHistoryStationsState.collectAsState()

    HistoryScreen(
        playHistory = playHistory.value,
        modifier = modifier,
       // onBackClick = onBackClick
    )
}


@VisibleForTesting
@Composable
internal fun HistoryScreen(
    playHistory: StationsUiState,
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
            titleRes = R.string.top_app_bar_preview_title,
            navigationIcon = Icons.Filled.Search,
            navigationIconContentDescription = stringResource(
                id = R.string.top_app_bar_preview_title
            ),
            actionIcon = Icons.Filled.MoreVert,
            actionIconContentDescription = stringResource(
                id = R.string.top_app_bar_preview_title
            )
        )
//        if(favoriteState.size > 0){
//            Text(text = favoriteState.get(0).name)
//        }

        when (playHistory) {
            StationsUiState.Loading ->
                LoadingWheel(
                    modifier = modifier,
                    contentDesc = stringResource(id = R.string.top_app_bar_preview_title),
                )
            is StationsUiState.Stations ->
                // Text(favoriteState.stations.get(0).station.name.toString())

                RadioItemFavorite(listOf(playHistory.stations), onImageClick = {})
            // RadioItem(listOf(favoriteState .stations))
            is StationsUiState.Empty -> Text("hello")
        }
    }
}