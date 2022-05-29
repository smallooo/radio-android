package com.google.samples.apps.nowinandroid.feature.foryou

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.google.samples.apps.nowinandroid.feature.foryou.ui.ShimmerAnimationType

@Composable
fun LocalRadioList(state: FoodCategoriesContract.State) {
    var shimmerAnimationType by remember { mutableStateOf(ShimmerAnimationType.FADE) }

    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = 100f,
        targetValue = 600f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1200, easing = LinearEasing),
            RepeatMode.Restart
        )
    )

    val colorAnim by transition.animateColor(
        initialValue = Color.LightGray.copy(alpha = 0.6f),
        targetValue = Color.LightGray,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            RepeatMode.Restart
        )
    )

    val list = if (shimmerAnimationType != ShimmerAnimationType.TRANSLATE) {
        listOf(colorAnim, colorAnim.copy(alpha = 0.5f))
    } else {
        listOf(Color.LightGray.copy(alpha = 0.6f), Color.LightGray)
    }

    val dpValue = if (shimmerAnimationType != ShimmerAnimationType.FADE) {
        translateAnim.dp
    } else {
        2000.dp
    }

    @Composable
    fun buttonColors(type: ShimmerAnimationType) = ButtonDefaults.buttonColors(
        containerColor = if (shimmerAnimationType == type)
            MaterialTheme.colorScheme.primary else Color.LightGray
    )

    if(state.isLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            ShimmerItem(list, dpValue.value, shimmerAnimationType == ShimmerAnimationType.VERTICAL)
            ShimmerItem(list, dpValue.value, shimmerAnimationType == ShimmerAnimationType.VERTICAL)
            ShimmerItem(list, dpValue.value, shimmerAnimationType == ShimmerAnimationType.VERTICAL)
            ShimmerItem(list, dpValue.value, shimmerAnimationType == ShimmerAnimationType.VERTICAL)
            ShimmerItem(list, dpValue.value, shimmerAnimationType == ShimmerAnimationType.VERTICAL)
        }
    }else{
        RadioItem(state.categories)
    }
}

@Composable
fun RadioItem(stateCategories : List<Country>){
    LazyColumn {
        itemsIndexed(
            items = stateCategories,
            itemContent = {index, item ->
                AnimatedListItem(tweet = item, index)
            }
        )
    }
}


@Composable
fun AnimatedListItem(tweet: Country, itemIndex: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = rememberImagePainter(
                data = "https://picsum.photos/id/${
                    itemIndex +
                            1
                }/200/200"
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(55.dp)
                .padding(4.dp)
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .weight(1f)
        ) {
            Text(
                text = tweet.name,
               // style = typography.h6.copy(fontSize = 16.sp),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = tweet.stationcount,
               // style = typography.subtitle2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
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