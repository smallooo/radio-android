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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hdmsh.common_compose.rememberFlowWithLifecycle

//fun <T : Any> LazyPagingItems<T>.isLoading() = loadState.refresh == LoadState.Loading

@Composable
internal fun SearchList(viewModel: SearchViewModel, listState: LazyListState) {
    SearchList(
        viewModel = viewModel,
//        audiosLazyPagingItems = rememberFlowWithLifecycle(viewModel.stateS.value).collectAsLazyPagingItems(),
        "a"
     //   listState = listState,
    )
}

@Composable
internal fun SearchList(
    viewModel: SearchViewModel,
    a: String,
    // audiosLazyPagingItems: LazyPagingItems<Audio>,
   // listState: LazyListState,
) {
    val viewState =viewModel.stateS
    //val searchFilter = viewState.filter
    val isLoading  = viewState.isLoading

//    val hasMultiplePagers = pagers.size > 1
//
//    if (pagersAreEmpty && !pagersAreLoading && refreshErrorState == null) {
//        // TODO: show different state when Albums or Artists selected and query is empty
//        FullScreenLoading(delayMillis = 100)
//        return
//    }
//
//    SearchListErrors(viewModel, viewState, refreshPagers, refreshErrorState, pagersAreEmpty, hasMultiplePagers)
//

    SearchListContent(isLoading, viewState )

}

@Composable
private fun SearchListErrors(
    viewModel: SearchViewModel,
    viewState: SearchViewState,
    refreshPagers: () -> Unit,
  //  scaffoldState: ScaffoldState = LocalScaffoldState.current,
) {
    

    // show snackbar if there's an error to show
//    LaunchedEffect(viewState.error) {
//        viewState.error?.let {
//            when (scaffoldState.snackbarHostState.showSnackbar(message, retryLabel, SnackbarDuration.Long)) {
//                SnackbarResult.ActionPerformed -> refreshPagers()
//                SnackbarResult.Dismissed -> viewModel.submitAction(SearchAction.ClearError)
//            }
//        }
//    }

    
}

@Composable
private fun SearchListContent(isLoading: Boolean, viewState : SearchViewModel.LocalStationsContract.State) {
//    LazyColumn(
//        modifier = Modifier.fillMaxSize()
//    ) {
        // TODO: examine why swiperefresh only works when first list item has some height

            Spacer(Modifier.height(1.dp))


        if (isLoading) {
            //  Log.e("aaa", "isLoading")
            //   Text("loading" + topVisitsState.isLoading + isLoading)
            //for (i in 1..5) ShimmerItem(list, dpValue.value, shimmerAnimationType == ShimmerAnimationType.VERTICAL)

                Box(modifier = Modifier.fillMaxSize()) {
                    Text("Loading...")

            }
        } else {
            viewState.localStations.forEach {

                    Text(text = it.name)

            }
        }
   // }
}





