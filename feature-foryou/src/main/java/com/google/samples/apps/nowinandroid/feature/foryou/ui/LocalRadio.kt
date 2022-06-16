package com.google.samples.apps.nowinandroid.feature.foryou

import android.annotation.SuppressLint
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import coil.compose.rememberImagePainter
import com.google.samples.app.nowinandroid.core.playback.isActive
import com.google.samples.apps.nowinandroid.common.compose.LocalPlaybackConnection

import com.google.samples.apps.nowinandroid.core.model.data.FollowableStation
import com.google.samples.apps.nowinandroid.feature.foryou.ui.ShimmerAnimationType
import com.google.samples.apps.nowinandroid.playback.PlaybackConnection
import com.hdmsh.common_compose.rememberFlowWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun LocalRadioList(
    pageType:PageType, param: String,
    viewModel: LocalRadioListViewModel = hiltViewModel(),
    playbackConnection: PlaybackConnection = LocalPlaybackConnection.current,
) {
    val uiState by viewModel.uiState.collectAsState()
    val shimmerAnimationType by remember { mutableStateOf(ShimmerAnimationType.FADE) }
    val transition = rememberInfiniteTransition()
    val playbackState by rememberFlowWithLifecycle(playbackConnection.playbackState)
    val nowPlaying by rememberFlowWithLifecycle(playbackConnection.nowPlaying)
    val isPlayerActive = (playbackState to nowPlaying).isActive

    val translateAnim by transition.animateFloat(
        initialValue = 100f,
        targetValue = 600f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1200, easing = LinearEasing),
            RepeatMode.Restart
        )
    )

    val colorAnim by transition.animateColor(
        initialValue = Color.LightGray.copy(alpha = 0.6f),
        targetValue = Color.LightGray,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            RepeatMode.Restart
        )
    )

    val list = if (shimmerAnimationType != ShimmerAnimationType.TRANSLATE) {
        listOf(colorAnim, colorAnim.copy(alpha = 0.5f))
    } else {
        listOf(Color.LightGray.copy(alpha = 0.6f), Color.LightGray)
    }

    val dpValue = if (shimmerAnimationType != ShimmerAnimationType.FADE) {
        translateAnim.dp
    } else {
        2000.dp
    }

    when (uiState) {
        StationsUiState.Loading ->
            for(i in 1..5) ShimmerItem(list, dpValue.value, shimmerAnimationType == ShimmerAnimationType.VERTICAL)
        is StationsUiState.Stations -> {
            RadioItem(listOf((uiState as StationsUiState.Stations).stations))
        }
        is StationsUiState.Empty  -> ShimmerItem(list, dpValue.value, shimmerAnimationType == ShimmerAnimationType.VERTICAL)
    }
}

@Composable
fun RadioItem(stateCategories : List<List<FollowableStation>>){
    LazyColumn {
        itemsIndexed(
            items = stateCategories.get(0),
            itemContent = {index, item ->
                AnimatedListItem(station = item, index)
            }
        )
    }
}

@Composable
fun AnimatedListItem(station: FollowableStation, itemIndex: Int, playbackConnection: PlaybackConnection = LocalPlaybackConnection.current,) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { playbackConnection.playAudio(station.station) }
    ) {
        Image(
            painter = rememberImagePainter(data = station.station.favicon),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(55.dp)
                .padding(4.dp)
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .weight(1f)
        ) {
            Text(
                text = station.station.name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = station.station.bitrate + "kbps",
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = null,
            tint = Color.LightGray,
            modifier = Modifier.padding(4.dp)
        )
    }
}