/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.hdmsh.core_ui_playback.components

import android.net.Uri
import android.support.v4.media.MediaMetadataCompat
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import com.google.samples.app.nowinandroid.core.playback.artwork
import com.google.samples.app.nowinandroid.core.playback.playPause
import com.google.samples.apps.nowinandroid.common.compose.LocalPlaybackConnection
import com.google.samples.apps.nowinandroid.core.ui.component.CoverImage
import com.google.samples.apps.nowinandroid.core.ui.component.coloredRippleClickable
import com.google.samples.apps.nowinandroid.core.ui.extensions.Callback
import com.google.samples.apps.nowinandroid.core.ui.theme.AppTheme
import com.google.samples.apps.nowinandroid.core.ui.theme.plainSurfaceColor
import com.google.samples.apps.nowinandroid.playback.PlaybackConnection


@Composable
internal fun PlaybackArtwork(
    artwork: Uri,
    contentColor: Color,
    nowPlaying: MediaMetadataCompat,
    modifier: Modifier = Modifier,
    onClick: Callback? = null,
    playbackConnection: PlaybackConnection = LocalPlaybackConnection.current,
) {
    CoverImage(
        data = artwork,
        shape = RectangleShape,
        backgroundColor = MaterialTheme.colors.plainSurfaceColor(),
        contentColor = contentColor,
        bitmapPlaceholder = nowPlaying.artwork,
        modifier = Modifier
            //.padding(horizontal = AppTheme.specs.paddingLarge)
            .then(modifier),
        imageModifier = Modifier.coloredRippleClickable(
            onClick = {
                if (onClick != null) onClick.invoke()
                else playbackConnection.mediaController?.playPause()
            },
            color = contentColor,
            rippleRadius = Dp.Unspecified,
        ),
    )
}
