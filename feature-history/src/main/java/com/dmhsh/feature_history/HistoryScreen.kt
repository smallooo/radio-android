package com.dmhsh.feature_history

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HistoryRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = hiltViewModel(),
) {

    HistoryScreen(
        modifier = modifier,
        onBackClick = onBackClick,
        )
}


@VisibleForTesting
@Composable
internal fun HistoryScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

}