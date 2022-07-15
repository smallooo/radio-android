/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.hdmsh.core_ui_playback.searching


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.AnimatedListItem
import com.hdmsh.common_compose.rememberFlowWithLifecycle


@Composable
internal fun SearchList(
    viewModel: SearchViewModel,
) {
    val viewState =viewModel.stateS
    if (viewState.isWaiting) {
    }else if(viewState.isLoading){
        Box(modifier = Modifier.fillMaxSize()) { Text("Loading...") }
    } else {
            viewState.localStations.forEachIndexed { index, item ->
                    AnimatedListItem(
                        viewModel,
                        station = item,
                        index,
                        onImageClick = {},
                        onPlayClick = {})
            }
    }
}

//@Composable
//private fun SearchListContent(isLoading: Boolean, viewState : SearchViewModel.LocalStationsContract.State) {
////    LazyColumn(
////        modifier = Modifier.fillMaxSize()
////    ) {
//        // TODO: examine why swiperefresh only works when first list item has some height
//
//            Spacer(Modifier.height(1.dp))
//
//
//        if (isLoading) {
//            //  Log.e("aaa", "isLoading")
//            //   Text("loading" + topVisitsState.isLoading + isLoading)
//            //for (i in 1..5) ShimmerItem(list, dpValue.value, shimmerAnimationType == ShimmerAnimationType.VERTICAL)
//
//                Box(modifier = Modifier.fillMaxSize()) {
//                    Text("Loading...")
//
//            }
//        } else {
//            viewState.localStations.forEach {
//
//                    Text(text = it.name)
//
//            }
//        }
//   // }
//}





