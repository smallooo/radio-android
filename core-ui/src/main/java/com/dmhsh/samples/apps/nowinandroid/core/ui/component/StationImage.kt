package com.dmhsh.samples.apps.nowinandroid.core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.dmhsh.samples.apps.nowinandroid.core.model.data.Station
import com.dmhsh.samples.apps.nowinandroid.core.ui.extensions.isNotNullandNotBlank
import com.dmhsh.samples.apps.nowinandroid.core.ui.theme.randomColor
import kotlin.random.Random

@Composable
fun StationImage(
    station: Station,
    onImageClick: (station: Station) -> Unit,
    favorite: Boolean
) {
    var favorite1 = favorite
    val color by remember { mutableStateOf(randomColor()) }

    if (station.favicon.isNotNullandNotBlank()) {
        CoverImage(
            data = station.favicon,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(55.dp)
                .padding(4.dp)
                .clickable {
                    val station1 = station
                    station1.favorited = !station1.favorited
                    onImageClick(station1)
                    favorite1 = !favorite1
                }
        )
    } else {
        Icon(
            painter = rememberVectorPainter(Icons.Filled.Radio),
            tint = color,
            contentDescription = null,
            modifier = Modifier
                .size(55.dp)
                .padding(6.dp)
                .clickable {
                    // onImageClick(station)
                    // favorite = !favorite
                }
        )
    }
}

