package com.dmhsh.samples.apps.nowinandroid.feature.foryou

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.dmhsh.samples.apps.nowinandroid.common.compose.LocalPlaybackConnection
import com.dmhsh.samples.apps.nowinandroid.core.model.data.Station
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.AnimatedListItem
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.CoverImage
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.FullScreenLoading

import com.dmhsh.samples.apps.nowinandroid.playback.PlaybackConnection

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun TopClickRadios(
    viewModel: TopClickViewModel = hiltViewModel(),
) {
    val topVisitsState = viewModel.state
    val isLoading  = topVisitsState.isLoading
    if (topVisitsState.isLoading) {
        FullScreenLoading()
    } else {
        RadioItemList(viewModel,
            listOf(topVisitsState.localStations),
            onImageClick = { Station ->
                Station.favorited = !Station.favorited
                viewModel.setFavoritedStation(Station)
            },
            onPlayClick = { Station ->
                Station.lastPlayedTime = System.currentTimeMillis().toString()

                viewModel.setPlayHistory(Station)
            }
        )
    }
}

@Composable
fun RadioItemList(viewModel: ViewModel, stateCategories: List<List<Station>>, onImageClick: (station: Station) -> Unit, onPlayClick: (station: Station) -> Unit) {
    LazyColumn {
        itemsIndexed(
            items = stateCategories.get(0),
            itemContent = { index, item ->
                AnimatedListItem(viewModel, station = item, index, onImageClick = onImageClick, onPlayClick = onPlayClick)
            }
        )
        item {
            Spacer(Modifier.height(180.dp))
        }
    }
}



