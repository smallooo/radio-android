package com.google.samples.apps.nowinandroid.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.samples.apps.nowinandroid.common.compose.LocalPlaybackConnection
import com.google.samples.apps.nowinandroid.core.model.data.FollowableStation
import com.google.samples.apps.nowinandroid.playback.PlaybackConnection

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