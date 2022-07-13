package com.dmhsh.samples.apps.nowinandroid.feature.foryou

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import com.dmhsh.samples.apps.nowinandroid.core.model.data.LanguageTag

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SearchStationsScreen(
    viewModel: SearchListViewModel = hiltViewModel(),
    onButtonSelect:(type: Int) -> Unit
) {
    val uiState = viewModel.state
    val isLoading = uiState.isLoading

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column() {
                Button(onClick = { onButtonSelect(0) }) {
                    Text(text = "Search by Tags")
                }

                Button(onClick = { onButtonSelect(1) }) {
                    Text(text = "Search by Countries")
                }

                Button(onClick = {
                    onButtonSelect(2)
                }) {
                    Text(text = "Search by Languages")
                }
            }


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