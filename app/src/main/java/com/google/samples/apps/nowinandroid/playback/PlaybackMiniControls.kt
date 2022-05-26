/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.google.samples.apps.nowinandroid.playback

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.samples.apps.nowinandroid.R

object PlaybackMiniControlsDefaults {
    val height = 56.dp
}

@Composable
fun PlaybackMiniControls(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    if(false) {
        PlaybackMiniControls1(contentPadding = contentPadding)
    }
}

@Composable
fun PlaybackMiniControls1(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    height: Dp = PlaybackMiniControlsDefaults.height,
) {
    Column {
        var controlsVisible by remember { mutableStateOf(true) }
        var nowPlayingVisible by remember { mutableStateOf(true) }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(height)
                .fillMaxWidth()
                .onGloballyPositioned {
                    val aspectRatio = it.size.height.toFloat() / it.size.width.toFloat()
                    controlsVisible = aspectRatio < 0.9
                    nowPlayingVisible = aspectRatio < 0.5
                }
                .padding(if (controlsVisible) contentPadding else PaddingValues())
        ) {

                PlaybackNowPlaying( coverOnly = !nowPlayingVisible)
        }
        PlaybackProgress(
            color = MaterialTheme.colors.onBackground,
        )
    }
}

@Composable
private fun PlaybackProgress(
    color: Color,
) {
    val sizeModifier = Modifier
        .height(2.dp)
        .fillMaxWidth()
    when {
        else -> {
            LinearProgressIndicator(
                color = color,
                modifier = sizeModifier
            )
        }
    }
}


@Composable
private fun RowScope.PlaybackNowPlaying(
    modifier: Modifier = Modifier,
    coverOnly: Boolean = false,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.weight(if (coverOnly) 3f else 7f),
    ) {
        Image(painter = painterResource(id = R.drawable.ic_100tb), contentDescription = "")
    }
}



