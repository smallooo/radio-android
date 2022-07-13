package com.dmhsh.samples.apps.nowinandroid.feature.foryou

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dmhsh.samples.app.nowinandroid.core.playback.isActive
import com.dmhsh.samples.apps.nowinandroid.common.compose.LocalPlaybackConnection
import com.dmhsh.samples.apps.nowinandroid.core.datastore.PreferencesStore
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.FullScreenLoading

import com.dmhsh.samples.apps.nowinandroid.core.ui.component.RadioItem
import com.dmhsh.samples.apps.nowinandroid.feature.foryou.ui.ShimmerAnimationType
import com.dmhsh.samples.apps.nowinandroid.playback.PlaybackConnection
import com.hdmsh.common_compose.rememberFlowWithLifecycle

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun LocalRadioList(
    viewModel: LocalRadioListViewModel = hiltViewModel(),
    //playbackConnection: PlaybackConnection = LocalPlaybackConnection.current,
) {

    val uiState by viewModel.localRadiosState.collectAsState()

    when (uiState) {
        StationsUiState.Loading -> {
            FullScreenLoading()
        }
        is StationsUiState.Stations -> {
            if (uiState is StationsUiState.Stations) (
                    RadioItem(viewModel,
                        (uiState as StationsUiState.Stations).stations,
                        onImageClick = { Station ->
                            Station.favorited = !Station.favorited
                            viewModel.setFavoritedStation(Station)
                        },
                        onPlayClick = { Station ->
                            Station.lastPlayedTime = System.currentTimeMillis().toString()
                            viewModel.setFavoritedStation(Station)
                        }
                    )
                    )
        }
    }
}