package com.dmhsh.feature_history

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.dmhsh.samples.apps.nowinandroid.core.ui.LoadingWheel
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.RadioTopAppBar
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.RadioItemFavorite

@Composable
fun HistoryRoute(
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val playHistory = viewModel.playHistoryStationsState.collectAsState()
        HistoryScreen(
            viewModel,
            playHistory = playHistory.value,
            modifier = modifier,
            // onBackClick = onBackClick
        )
}


@VisibleForTesting
@Composable
internal fun HistoryScreen(
    viewModel: HistoryViewModel,
    playHistory: StationsUiState,
    modifier: Modifier = Modifier,
) {

    Spacer(
        // TODO: Replace with windowInsetsTopHeight after
        //       https://issuetracker.google.com/issues/230383055
        Modifier.windowInsetsPadding(
            WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
        )
    )

    Scaffold(
        modifier = modifier,
        topBar = {
            RadioTopAppBar(
                titleRes = R.string.top_app_bar_preview_title,
                navigationIcon = Icons.Filled.Search,
                navigationIconContentDescription = stringResource(
                    id = R.string.top_app_bar_preview_title
                ),
                actionIcon = Icons.Filled.MoreVert,
                actionIconContentDescription = stringResource(
                    id = R.string.top_app_bar_preview_title
                ),

                modifier = Modifier.windowInsetsPadding(
                WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
            )
            )
        }
    ) { padding ->
        val pad = padding

        when (playHistory) {
            StationsUiState.Loading ->
                LoadingWheel(
                    modifier = modifier,
                    contentDesc = stringResource(id = R.string.top_app_bar_preview_title),
                )
            is StationsUiState.Stations ->
                Column{
                    Text(text = "hello1",color = MaterialTheme.colors.secondary)
                    Text(text = "hello2",color = MaterialTheme.colors.onBackground)
                    Text(text = "hello3",color = MaterialTheme.colors.background)
                    Text(text = "hello4",color = MaterialTheme.colors.surface)
                    Text(text = "hello5",color = MaterialTheme.colors.onSurface)
                    Text(text = "hello6",color = MaterialTheme.colors.error)
                    Text(text = "hello7",color = MaterialTheme.colors.onError)
                    Text(text = "hello8",color = MaterialTheme.colors.onPrimary)
                    Text(text = "hello9",color = MaterialTheme.colors.primary)
                    Text(text = "hello10",color = MaterialTheme.colors.primaryVariant)
                    Text(text = "hello11",color = MaterialTheme.colors.secondaryVariant)
                    RadioItemFavorite(viewModel, listOf(playHistory.stations), onImageClick = {})
                }
            is StationsUiState.Empty -> Text("hello")
        }
    }
}