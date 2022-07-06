package com.dmhsh.samples.apps.nowinandroid.core.ui.component

import android.util.Log
import android.widget.ScrollView
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.dmhsh.samples.apps.nowinandroid.common.compose.LocalPlaybackConnection
import com.dmhsh.samples.apps.nowinandroid.core.datastore.PreferencesStore
import com.dmhsh.samples.apps.nowinandroid.core.model.data.FollowableStation
import com.dmhsh.samples.apps.nowinandroid.playback.PlaybackConnection
import com.hdmsh.common_compose.rememberFlowWithLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import tm.alashow.i18n.R

//fun Modifier.scrollEnabled(
//    enabled: Boolean,
//) = nestedScroll(
//    connection = object : NestedScrollConnection {
//        override fun onPreScroll(
//            available: Offset,
//            source: NestedScrollSource
//        ): Offset = if(enabled) Offset.Zero else available
//    }
//)

@Composable
fun RadioItemFavorite(stateCategories : List<List<FollowableStation>>,
                      listState: LazyListState = rememberLazyListState(),
                      onImageClick: () -> Unit){

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(8.dp),
    ) {
        interestsList(stateCategories, onImageClick)
    }
//    if (stateCategories.isEmpty()) {
//        item {
//            Delayed {
//                EmptyErrorBox(
//                    message = stringResource(R.string.downloads_empty),
//                    retryVisible = false,
//                    modifier = Modifier.fillParentMaxHeight()
//                )
//            }
//        }
//    }
//
//
//
////    Column(modifier = Modifier.fillMaxSize()){
//    LazyColumn {
//            itemsIndexed(
//                items = stateCategories.get(0),
//                itemContent = { index, item ->
//                    AnimatedListFavoriteItem(station = item, index, onImageClick)
//                }
//            )
//        }
  //  }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.interestsList(
    stateCategories: List<List<FollowableStation>>,
    onImageClick: () -> Unit
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
                AnimatedListFavoriteItem(station = item, index, onImageClick)
            }
        }

        item {
            Spacer(modifier = Modifier.height(160.dp))
        }
    }
}



@Composable
fun AnimatedListFavoriteItem(station: FollowableStation, itemIndex: Int, onImageClick: () -> Unit) {
    val playbackConnection: PlaybackConnection = LocalPlaybackConnection.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { playbackConnection.playAudio(station.station) },

    ) {
        CoverImage(
            data = station.station.favicon,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(55.dp)
                .padding(4.dp)
                .clickable {
                    Log.e("aaa", "onImageClick3")
                    onImageClick


                }
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .weight(1f)
        ) {
            Text(
                text = station.station.name,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface
            )
            Text(
                text = station.station.bitrate + "kbps",
                style = MaterialTheme.typography.body1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colors.onSurface
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