package com.dmhsh.samples.apps.nowinandroid.feature.foryou


import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
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
import com.dmhsh.samples.apps.nowinandroid.core.model.data.LanguageTag
import com.dmhsh.samples.apps.nowinandroid.core.model.data.StationsTag
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.CoverImage
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.LanguageItems
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.RadioItem
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.TagItems

import com.dmhsh.samples.apps.nowinandroid.feature.foryou.ui.ShimmerAnimationType

@Composable
fun LanguageListScreen( viewModel: LanguageListViewModel = hiltViewModel(),onTagSelect:(stationsTag: LanguageTag) -> Unit) {

    val uiState = viewModel.languageListState.collectAsState()
    val shimmerAnimationType by remember { mutableStateOf(ShimmerAnimationType.FADE) }

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

    when (uiState.value) {
        LanguageUiState.Loading ->
            for(i in 1..5) ShimmerItem(list, dpValue.value, shimmerAnimationType == ShimmerAnimationType.VERTICAL)
        is LanguageUiState.Tags -> {
            LanguageItems(
               (uiState.value as LanguageUiState.Tags).tags,
                onItemClick = {
                    onTagSelect(it)

                }
            )
        }
        is LanguageUiState.Empty -> ShimmerItem(list, dpValue.value, shimmerAnimationType == ShimmerAnimationType.VERTICAL)
    }
}

