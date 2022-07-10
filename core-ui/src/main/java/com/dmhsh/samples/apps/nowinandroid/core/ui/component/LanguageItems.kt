package com.dmhsh.samples.apps.nowinandroid.core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dmhsh.samples.apps.nowinandroid.common.compose.LocalPlaybackConnection
import com.dmhsh.samples.apps.nowinandroid.core.model.data.LanguageTag
import com.dmhsh.samples.apps.nowinandroid.playback.PlaybackConnection

@Composable
fun LanguageItems(stateCategories : List<LanguageTag>, onItemClick: (stationsTag: LanguageTag) -> Unit){
    LazyColumn {
        itemsIndexed(
            items = stateCategories,
            itemContent = {index, item ->
                AnimatedLanguageListItem(
                    stationTag = item,
                    index,
                    onItemClick = onItemClick)
            }
        )
    }
}

@Composable
fun AnimatedLanguageListItem(stationTag: LanguageTag, itemIndex: Int, onItemClick: (stationsTag: LanguageTag) -> Unit) {
    val playbackConnection: PlaybackConnection = LocalPlaybackConnection.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().clickable { onItemClick(stationTag)}.padding(8.dp, 0.dp, 16.dp, 0.dp) ,
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .weight(1f)
        ) {
            Text(
                text = stationTag.name,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface
            )

        }

        Text(
            text = stationTag.stationcount,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface
        )
    }
}