package com.dmhsh.samples.apps.nowinandroid.feature.foryou


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Text

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.dmhsh.samples.apps.nowinandroid.core.model.data.Country
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.CoverImage
import com.dmhsh.samples.apps.nowinandroid.feature.foryou.ui.ShimmerAnimationType

@Composable
fun CountryList( viewModel: CountryViewModel = hiltViewModel(), onCountrySelect:(country: Country) -> Unit) {
    val state =  viewModel.state
    val loading = state.isLoading
//    val shimmerAnimationType by remember { mutableStateOf(ShimmerAnimationType.FADE) }

 //   val transition = rememberInfiniteTransition()
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

//    @Composable
//    fun buttonColors(type: ShimmerAnimationType) = ButtonDefaults.buttonColors(
//        containerColor = if (shimmerAnimationType == type)
//            MaterialTheme.colorScheme.primary else Color.LightGray
//    )

    if(loading) {
        //for (i in 1..5) ShimmerItem(list, dpValue.value, shimmerAnimationType == ShimmerAnimationType.VERTICAL)
        Box(modifier = Modifier.fillMaxSize()) {
            Text("Loading...")
        }
    }else{
//        Button(onClick = {onCountrySelect(state.categories.get(1)) }) {
//
//        }
        CountryItem(state.categories,onCountrySelect)
    }
}

@Composable
fun CountryItem(stateCategories : List<Country>, onCountrySelect: (country: Country) -> Unit){
    LazyColumn {
        itemsIndexed(
            items = stateCategories,
            itemContent = {index, item ->
                AnimatedCountryListItem(tweet = item, index,onCountrySelect)
            }
        )
    }
}

@Composable
fun AnimatedCountryListItem(tweet: Country, itemIndex: Int, onCountrySelect: (country: Country) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onCountrySelect(tweet) }
    ) {
        CoverImage(
            data = "https://picsum.photos/id/${itemIndex + 1}/200/200",
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
                text = tweet.name?:"",
               // style = typography.h6.copy(fontSize = 16.sp),
                color = MaterialTheme.colors.onSurface
            )
            Text(
                text = tweet.stationcount?:"",
               // style = typography.subtitle2,
                maxLines = 1,
                color = MaterialTheme.colors.onSurface
                //overflow = TextOverflow.Ellipsis
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