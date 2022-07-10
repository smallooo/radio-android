package com.dmhsh.samples.apps.nowinandroid.feature.foryou

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.dmhsh.samples.apps.nowinandroid.core.model.data.StationsTag
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.TagItems

@Composable
fun TagListScreen( viewModel: TagListViewModel = hiltViewModel(),
                   onTagSelect:(stationsTag: StationsTag) -> Unit) {

    val uiState by viewModel.tagListState.collectAsState()

    when (uiState) {
        TagUiState.Loading ->
            Box(modifier = Modifier.fillMaxSize()) { Text("Loading...",color = MaterialTheme.colors.onSurface) }
        is TagUiState.Tags -> {
            TagItems((uiState as TagUiState.Tags).tags, onItemClick = { onTagSelect(it) })
        }
        is TagUiState.Empty -> {
            Box(modifier = Modifier.fillMaxSize()) { Text("Loading...") }
        }
    }
}


