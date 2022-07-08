package com.dmhsh.samples.apps.nowinandroid.feature.foryou

import android.annotation.SuppressLint
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import com.dmhsh.samples.apps.nowinandroid.feature.foryou.ui.ShimmerAnimationType

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SearchStationsScreen(viewModel: SearchListViewModel = hiltViewModel()) {
    val uiState = viewModel.state
    val isLoading = uiState.isLoading
//    val shimmerAnimationType by remember { mutableStateOf(ShimmerAnimationType.FADE) }
//    val transition = rememberInfiniteTransition()


//    val translateAnim by transition.animateFloat(
//        initialValue = 100f,
//        targetValue = 600f,
//        animationSpec = infiniteRepeatable(
//            tween(durationMillis = 1200, easing = LinearEasing),
//            RepeatMode.Restart
//        )
//    )
//
//    val colorAnim by transition.animateColor(
//        initialValue = Color.LightGray.copy(alpha = 0.6f),
//        targetValue = Color.LightGray,
//        animationSpec = infiniteRepeatable(
//            tween(durationMillis = 1200, easing = FastOutSlowInEasing),
//            RepeatMode.Restart
//        )
//    )

//    val list = if (shimmerAnimationType != ShimmerAnimationType.TRANSLATE) {
//        listOf(colorAnim, colorAnim.copy(alpha = 0.5f))
//    } else {
//        listOf(Color.LightGray.copy(alpha = 0.6f), Color.LightGray)
//    }
//
//    val dpValue = if (shimmerAnimationType != ShimmerAnimationType.FADE) {
//        translateAnim.dp
//    } else {
//        2000.dp
//    }

    if (isLoading) {
        //for (i in 1..5) ShimmerItem(list, dpValue.value, shimmerAnimationType == ShimmerAnimationType.VERTICAL)
        Box(modifier = Modifier.fillMaxSize()) {
            Text("Loading...")
        }
    } else {
        RadioItemList(viewModel,
            listOf(uiState.localStations),
            onImageClick = { Station ->

                Station.favorited = !Station.favorited
                viewModel.setFavoritedStation(Station)
            },
            onPlayClick = { Station ->
                Station.lastPlayedTime = System.currentTimeMillis().toString()
                viewModel.setPlayHistory(Station)

            }
        )
    }
}