package com.dmhsh.samples.apps.nowinandroid.core.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dmhsh.samples.app.nowinandroid.core.playback.isError
import com.dmhsh.samples.app.nowinandroid.core.playback.isPlayEnabled
import com.dmhsh.samples.app.nowinandroid.core.playback.isPlaying
import com.dmhsh.samples.apps.nowinandroid.common.compose.LocalPlaybackConnection
import com.dmhsh.samples.apps.nowinandroid.core.model.data.Station
import com.dmhsh.samples.apps.nowinandroid.core.ui.theme.AppTheme
import com.dmhsh.samples.apps.nowinandroid.playback.PlaybackConnection


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
 fun localStationControl(
    station: Station,
    cardHeight: Dp,
    playbackConnection: PlaybackConnection = LocalPlaybackConnection.current,
    onFavoriteClick: (station: Station) -> Unit,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = cardHeight),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        androidx.compose.material3.IconButton(
            onClick = {
               playbackConnection.playAudio(station)
            },
            modifier = Modifier
                .size(39.dp)
                .weight(5f),
            //rippleRadius = smallRippleRadius,
        ) {}

        androidx.compose.material3.IconButton(
            onClick = {
                // if(playbackConnection.)
                playbackConnection.playAudio(station)
            },
            modifier = Modifier
                .size(80.dp)
                .weight(8f),
            // rippleRadius = 35.dp,
        ) {
            Icon(
                painter = rememberVectorPainter(
//                    when {
//                        playbackConnection.playbackState.value.isError -> Icons.Filled.ErrorOutline
//                        playbackConnection.playbackState.value.isPlaying -> Icons.Filled.PauseCircleFilled
//                        playbackConnection.playbackState.value.isPlayEnabled -> Icons.Filled.PlayCircleFilled
//                        else -> Icons.Filled.PlayCircleFilled
//                    }
                    Icons.Filled.PlayCircleFilled
                ),
                // tint = contentColor,
                modifier = Modifier.fillMaxSize(),
                contentDescription = null
            )
        }

        Spacer(Modifier.width(AppTheme.specs.padding))


        IconButton(
            onClick = {
                   //favorited = !favorited
                   onFavoriteClick(station)
            },
            modifier = Modifier
                .size(39.dp)
                .weight(5f),
              rippleRadius = 30.dp,
        ) {
            Icon(
                imageVector = if (false) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                //tint = contentColor,
                modifier = Modifier.fillMaxSize(),
                contentDescription = null
            )
        }
    }
}