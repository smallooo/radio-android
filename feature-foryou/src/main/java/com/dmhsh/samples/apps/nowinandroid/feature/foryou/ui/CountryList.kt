package com.dmhsh.samples.apps.nowinandroid.feature.foryou


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.dmhsh.samples.apps.nowinandroid.core.model.data.Country
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.CoverImage
import com.dmhsh.samples.apps.nowinandroid.feature.foryou.ui.ShimmerAnimationType

@Composable
fun CountryList( viewModel: CountryViewModel = hiltViewModel(), onCountrySelect:(country: Country) -> Unit) {
    val state =  viewModel.state
    val loading = state.isLoading

    if(loading) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text("Loading...")
        }
    }else{
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

    Spacer(modifier = Modifier.height(180.dp))
}

@Composable
fun AnimatedCountryListItem(tweet: Country, itemIndex: Int, onCountrySelect: (country: Country) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 8.dp)
            .clickable { onCountrySelect(tweet) }
    ) {
        CoverImage(
            data = "https://picsum.photos/id/${itemIndex + 9}/200/200",
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
                style = typography.h6.copy(fontSize = 16.sp),
                color = MaterialTheme.colors.onSurface
            )

            Text(
                text = tweet.stationcount?:"",
                style = typography.subtitle2,
                maxLines = 1,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(top = 4.dp)
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