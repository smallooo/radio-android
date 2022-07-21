package com.dmhsh.samples.apps.nowinandroid.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dmhsh.samples.apps.nowinandroid.core.model.data.Station


@Composable
fun HorizontalListItem(
    station: Station,
    modifier: Modifier = Modifier,
    recommendedClick: (station: Station) -> Unit,
) {
    Material3Card(
        shape = androidx.compose.material.MaterialTheme.shapes.medium,
        modifier = modifier
            .size(180.dp, 180.dp),
            //.testTag("${TestTags.HOME_SCREEN_LIST_ITEM}-${item.id}"),
        elevation = 2.dp
    ) {
        Box(modifier = Modifier.clickable(onClick = {
            recommendedClick(station)
        })) {
            CoverImage(
                data = station.favicon,
                contentScale = ContentScale.FillHeight,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
            )

            Column(modifier = Modifier.padding(16.dp).align(Alignment.BottomEnd)) {
                Text(
                    text = station.name,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
//                Text(
//                    text = station.hls,
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis,
//                    style = MaterialTheme.typography.bodyMedium
//                )
//                Text(
//                    text = station.changeuuid,
//                    style = MaterialTheme.typography.titleSmall
//                )
            }
        }
    }
}
