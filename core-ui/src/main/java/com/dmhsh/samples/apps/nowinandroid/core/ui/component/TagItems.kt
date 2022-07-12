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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dmhsh.samples.apps.nowinandroid.core.model.data.StationsTag


@Composable
fun TagItems(stateCategories : List<StationsTag>, onItemClick: (stationsTag: StationsTag) -> Unit){
    LazyColumn {
        itemsIndexed(
            items = stateCategories,
            itemContent = {index, item ->
                AnimatedTagListItem(stationTag = item, index, onItemClick = onItemClick)
            }
        )
    }
}

@Composable
fun AnimatedTagListItem(stationTag: StationsTag, itemIndex: Int, onItemClick: (stationsTag: StationsTag) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().clickable { onItemClick(stationTag) }.padding(8.dp, 8.dp, 16.dp, 0.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp).weight(1f)) {
            Text(
                text = stationTag.name,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface
            )
        }

        Text(
            text = stationTag.stationcount,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface,
            textAlign = TextAlign.End
        )
    }
}