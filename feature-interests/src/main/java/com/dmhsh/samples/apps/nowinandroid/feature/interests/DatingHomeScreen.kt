package com.dmhsh.samples.apps.nowinandroid.feature.interests

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dmhsh.samples.apps.nowinandroid.core.model.data.Station
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.CoverImage
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.localStationControl
import kotlin.random.Random

fun Modifier.gradientBackground(
    colors: List<Color>,
    brushProvider: (List<Color>, Size) -> Brush
): Modifier = composed {
    var size by remember { mutableStateOf(Size.Zero) }
    val gradient = remember(colors, size) { brushProvider(colors, size) }
    drawWithContent {
        size = this.size
        drawRect(brush = gradient)
        drawContent()
    }
}


fun Modifier.verticalGradientBackground(
    colors: List<Color>
) = gradientBackground(colors) { gradientColors, size ->
    Brush.verticalGradient(
        colors = gradientColors,
        startY = 0f,
        endY = 30f
    )
}

@Composable
fun DatingHomeScreen(
    stations: ArrayList<Station>,
    favoriteStationstViewModel: FavoriteStationstViewModel
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val cardHeight = screenHeight - 260.dp

    Surface(modifier = Modifier.fillMaxSize()) {
        val boxModifier = Modifier
        Box(
            modifier = boxModifier.verticalGradientBackground(
                listOf(
                    Color.White,
                    Color.Yellow.copy(alpha = 0.2f)
                )
            )
        ) {
            val listEmpty = remember { mutableStateOf(false) }
            DatingLoader(modifier = boxModifier)
            stations.forEachIndexed { index, station ->
                DraggableCard(
                    item = station,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(cardHeight)
                        .padding(
                            //top = 16.dp + (index + 2).dp,
                            bottom = 16.dp,
                            start = 16.dp,
                            end = 16.dp
                        ),
                    onSwiped = { _, swipedAlbum ->
                        if (stations.isNotEmpty()) {
                            stations.remove(swipedAlbum)
                            if (stations.isEmpty()) {
                                listEmpty.value = true
                            }
                        }
                    }
                ) { CardContent(station) }

                localStationControl(station, cardHeight, onFavoriteClick = { station ->
                    val _station = station
                    _station.favorited = !station.favorited
                    favoriteStationstViewModel.setFavoritedStation(_station)
                })
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = cardHeight)
                    .alpha(animateFloatAsState(if (listEmpty.value) 0f else 1f).value)
            ) {

            }
        }
    }
}



@Composable
fun CardContent(station: Station) {
    Column {
        CoverImage(
            data = station.favicon,
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier.weight(1f)
        )
        Row(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
            Text(
                text = station.name,
                style = typography.h6,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .weight(1f),
                textAlign = TextAlign.Start
            )
            Icon(
                imageVector = Icons.Outlined.Place,
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                tint = Color.Cyan,
                contentDescription = null
            )
            Text(
                text = "${Random.nextInt(1, 15)} km",
                style = typography.body2,
                color = Color.Cyan
            )
        }
        Text(
            text = "Age: ${Random.nextInt(21, 30)}, Casually browsing..",
            style = typography.subtitle2,
            modifier = Modifier.padding(bottom = 4.dp, start = 16.dp, end = 16.dp)
        )
        Text(
            text = "Miami, Florida",
            style = typography.subtitle2,
            modifier = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
        )
    }
}

@Composable
fun DatingLoader(modifier: Modifier) {
    Box(
        contentAlignment = Alignment.Center, modifier = modifier
            .fillMaxSize()
            .clip(CircleShape)
    ) {
        MultiStateAnimationCircleFilledCanvas(Color.Cyan, 400f)
        Image(
            painter = painterResource(id = R.drawable.country),
            modifier = modifier
                .size(50.dp)
                .clip(CircleShape),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
    }
}



