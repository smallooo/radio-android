package com.dmhsh.samples.apps.nowinandroid.core.ui.component

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import coil.compose.rememberImagePainter
import com.dmhsh.samples.apps.nowinandroid.common.compose.LocalPlaybackConnection
import com.dmhsh.samples.apps.nowinandroid.core.datastore.PreferencesStore
import com.dmhsh.samples.apps.nowinandroid.core.model.data.FollowableStation
import com.dmhsh.samples.apps.nowinandroid.core.model.data.Station
import com.dmhsh.samples.apps.nowinandroid.playback.PlaybackConnection
import com.hdmsh.common_compose.rememberFlowWithLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

@Composable
fun RadioItem(viewModel: ViewModel, stateCategories : List<Station>, onImageClick: (station: Station) -> Unit, onPlayClick: (station: Station) -> Unit){
    LazyColumn {
        itemsIndexed(
            items = stateCategories,
            itemContent = {index, item ->
                AnimatedListItem(
                    station = item,
                    index,
                    onImageClick = onImageClick,
                    onPlayClick = onPlayClick)
            }
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedListItem(station: Station, itemIndex: Int, onImageClick: (station: Station) -> Unit, onPlayClick: (station: Station) -> Unit) {
    val playbackConnection: PlaybackConnection = LocalPlaybackConnection.current
    var expanded by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            playbackConnection.playAudio(station)
            onPlayClick(station) },
    ) {
        CoverImage(
            data =  station.favicon,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(55.dp)
                .padding(4.dp)
                .clickable {
                    onImageClick(station)
                }
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .weight(1f)
        ) {
            Text(
                text = station.name,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface
            )
            Text(
                text = station.bitrate + "kbps",
                style = MaterialTheme.typography.body1,
                maxLines = 1,
                color = MaterialTheme.colors.onSurface,
                overflow = TextOverflow.Ellipsis
            )
        }
        Icon(
            imageVector = if(expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
            contentDescription = null,
            tint = Color.LightGray,
            modifier = Modifier
                .padding(4.dp)
                .clickable { expanded = !expanded }
        )
    }


    AnimatedVisibility(visible = expanded) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {


            Row() {
                listOf(station.tags).forEach{ tag ->
                    Text(text = tag)
                }
            }


            Row() {
                Icon(imageVector = Icons.Default.Home, contentDescription = "")
                Icon(imageVector = Icons.Default.Share, contentDescription = "")
                Icon(imageVector = Icons.Default.Star, contentDescription = "")
                Icon(imageVector = Icons.Default.PunchClock, contentDescription = "")
                Icon(imageVector = Icons.Default.Add, contentDescription = "")

                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "")
            }
//
//
//
//
//                Material3Card(
//                    backgroundColor = MaterialTheme.colors.background, modifier = Modifier
//                        .size(80.dp)
//                        .padding
//                            (8.dp)
//                        .animateEnterExit(
//                            enter = slideInHorizontally { it },
//                            exit = ExitTransition.None
//                        )
//                ) {
//
//                }
        }
    }

}