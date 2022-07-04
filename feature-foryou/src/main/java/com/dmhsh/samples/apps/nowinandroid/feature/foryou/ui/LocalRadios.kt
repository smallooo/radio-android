package com.dmhsh.samples.apps.nowinandroid.feature.foryou

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dmhsh.samples.app.nowinandroid.core.playback.isActive
import com.dmhsh.samples.apps.nowinandroid.common.compose.LocalPlaybackConnection
import com.dmhsh.samples.apps.nowinandroid.core.datastore.PreferencesStore

import com.dmhsh.samples.apps.nowinandroid.core.ui.component.RadioItem
import com.dmhsh.samples.apps.nowinandroid.feature.foryou.ui.ShimmerAnimationType
import com.dmhsh.samples.apps.nowinandroid.playback.PlaybackConnection
import com.hdmsh.common_compose.rememberFlowWithLifecycle

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun LocalRadioList(
    viewModel: LocalRadioListViewModel = hiltViewModel(),
    playbackConnection: PlaybackConnection = LocalPlaybackConnection.current,
    ) {
    val preferences = PreferencesStore(LocalContext.current)
    val preString = rememberFlowWithLifecycle(preferences.get("aaa",ArrayList<String>()))
    val exampleEntities: ArrayList<String> by preString.collectAsState(initial = ArrayList<String>())
    val uiState by viewModel.localRadiosState.collectAsState()


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
        StationsUiState.Loading -> {
            for (i in 1..5) ShimmerItem(
                list,
                dpValue.value,
                shimmerAnimationType == ShimmerAnimationType.VERTICAL
            )
        }
        is StationsUiState.Stations -> {
           // Log.e("aaa", "refresh stations")
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
        is StationsUiState.Empty -> ShimmerItem(list, dpValue.value, shimmerAnimationType == ShimmerAnimationType.VERTICAL)
    }
}