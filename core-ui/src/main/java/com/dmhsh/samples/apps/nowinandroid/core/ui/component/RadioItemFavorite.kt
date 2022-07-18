package com.dmhsh.samples.apps.nowinandroid.core.ui.component

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.dmhsh.samples.apps.nowinandroid.common.compose.LocalPlaybackConnection
import com.dmhsh.samples.apps.nowinandroid.core.model.data.FollowableStation
import com.dmhsh.samples.apps.nowinandroid.core.model.data.Station
import com.dmhsh.samples.apps.nowinandroid.core.util.IntentUtils
import com.dmhsh.samples.apps.nowinandroid.playback.PlaybackConnection
import tm.alashow.i18n.R

@Composable
fun RadioItemFavorite(
    viewModel: ViewModel,
    stateCategories : List<List<FollowableStation>>,
    listState: LazyListState = rememberLazyListState(),
    onImageClick: (station: Station) -> Unit){

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(8.dp),
    ) {
        interestsList(viewModel, stateCategories, onImageClick)
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.interestsList(
    viewModel: ViewModel,
    stateCategories: List<List<FollowableStation>>,
    onImageClick: (station: Station) -> Unit
) {
    if (stateCategories.get(0).isNullOrEmpty()) {
        item {
            Delayed {
                EmptyErrorBox(
                    message = stringResource(R.string.downloads_empty),
                    retryVisible = false,
                    modifier = Modifier.fillParentMaxHeight()
                )
            }
        }
    }else {
        itemsIndexed(stateCategories.get(0), { _, it -> it.station }) { index, item ->
            Column {
                AnimatedListFavoriteItem(viewModel,station = item.station, index, onImageClick)
            }
        }

        item {
            Spacer(modifier = Modifier.height(160.dp))
        }
    }
}



@Composable
fun AnimatedListFavoriteItem(viewModel: ViewModel, station: Station, itemIndex: Int, onImageClick: (station: Station) -> Unit) {
    val playbackConnection: PlaybackConnection = LocalPlaybackConnection.current
    var expanded by remember { mutableStateOf(false) }
    var favorite by remember { mutableStateOf(station.favorited) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 8.dp)
            .clickable {
                playbackConnection.playAudio(station)
            }
    ) {
        CoverImage(
            data =  station.favicon,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(55.dp)
                .padding(4.dp)
                .clickable {
                    val station1 = station
                    station1.favorited = ! station1.favorited
                     onImageClick(station1)
                     favorite = !favorite
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
                .padding(8.dp)
                .size(32.dp)
                .clickable { expanded = !expanded }
        )
    }

    AnimatedVisibility(visible = expanded) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            if(station.tags.isNotBlank()) {
                Row(modifier = Modifier.padding(start = 8.dp, top = 0.dp)) {
                    listOf(station.tags).forEach { tag ->
                        InterestTag(text = tag)
                    }
                }
            }
            SocialRow( station, favorite , onImageClick)
        }
    }
}

@Composable
fun SocialRow( station: Station, favorite: Boolean, onImageClick: (station: Station) -> Unit) {
    Material3Card(elevation = 1.dp, modifier = Modifier.padding(0.dp), backgroundColor = MaterialTheme.colors.surface) {
        val context = LocalContext.current
        val shareTitle = stringResource(R.string.share_action)
        var favorite1 by remember { mutableStateOf(favorite) }

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            if(station.homepage.isNotBlank()) {
                IconButton(onClick = { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(station.homepage))) },) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        tint = MaterialTheme.colors.secondary)
                }
            }

            IconButton(onClick = {
                val share = Intent(Intent.ACTION_SEND)
                share.type = "text/plain"
                share.putExtra(Intent.EXTRA_SUBJECT, station.name)
                share.putExtra(Intent.EXTRA_TEXT, station.name + "        " +  station.url_resolved)
                val title: String = shareTitle
                val chooser = Intent.createChooser(share, title)
                IntentUtils.startActivity(context, chooser)
            }) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null,
                    tint = MaterialTheme.colors.secondary,
                )
            }

            IconButton(onClick = {
                val station1 = station
                station1.favorited = !station1.favorited
                onImageClick(station1)
                favorite1 = !favorite1
            }) {
                Icon(
                    imageVector = if (favorite1) Icons.Default.Star else Icons.Default.StarBorder,
                    contentDescription = null,
                    tint = MaterialTheme.colors.secondary,
                )
            }
        }
    }
}