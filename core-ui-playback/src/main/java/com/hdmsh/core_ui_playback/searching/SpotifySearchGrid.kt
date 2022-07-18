package com.hdmsh.core_ui_playback.searching

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun SpotifySearchGrid( onSearchSelect: (content: String) -> Unit) {
    val items = remember { StationSearchDataProvider.content }
    //This is not Lazy at the moment Soon we will have LazyLayout coming then will
    //Update it so we have better performance
    VerticalGrid {
        items.forEach {
            SpotifySearchGridItem(it,onSearchSelect)
        }
    }
}

@Composable
fun SpotifySearchGridItem(album: Album, onSearchSelect: (content: String) -> Unit) {
    val context = LocalContext.current
    val imageBitmap = ImageBitmap.imageResource(context.resources, album.imageId).asAndroidBitmap()
    val swatch = remember(album.id) { imageBitmap.generateDominantColorState() }
    val dominantGradient =
        remember { listOf(Color(swatch.rgb), Color(swatch.rgb).copy(alpha = 0.6f)) }
    Row(
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = {
                onSearchSelect(album.song)
                //Disclaimer: We should pass event top level and there should startActivity
               // context.startActivity(SpotifyDetailActivity.newIntent(context, album))
            })
            .height(100.dp)
            .clip(RoundedCornerShape(8.dp))
            .horizontalGradientBackground(dominantGradient),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = album.song,
            style = typography.h6.copy(fontSize = 14.sp),
            modifier = Modifier.padding(8.dp)
        )
        Image(
            painter = painterResource(id = album.imageId),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .size(70.dp)
                .align(Alignment.Bottom)
                .graphicsLayer(translationX = 40f, rotationZ = 32f, shadowElevation = 16f)
        )
    }
}
