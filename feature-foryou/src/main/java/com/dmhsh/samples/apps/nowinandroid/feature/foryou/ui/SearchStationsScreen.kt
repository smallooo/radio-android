package com.dmhsh.samples.apps.nowinandroid.feature.foryou

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dmhsh.samples.apps.nowinandroid.core.model.data.LanguageTag
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.FullScreenLoading
import com.dmhsh.samples.apps.nowinandroid.core.ui.extensions.isNotNullandNotBlank
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@SuppressLint("StateFlowValueCalledInComposition", "CoroutineCreationDuringComposition")
@Composable
fun SearchStationsScreen(
    query: String,
    viewModel: SearchListViewModel = hiltViewModel(),
    onButtonSelect:(type: Int) -> Unit
) {
    val uiState = viewModel.state
    val isInitStatus = uiState.initStatus
    val isLoading = uiState.isLoading

    if (isInitStatus) {
        if(query.isNotNullandNotBlank()) {
            viewModel.getTagearch("bytag", query)
            onButtonSelect(3)

        }

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                Modifier.fillMaxWidth().padding(top = 60.dp)
            ) {
                Button(onClick = { onButtonSelect(0) },
                    modifier = Modifier.fillMaxWidth().padding(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                ) {
                    Text(text = "Search by Tags", color = MaterialTheme.colors.secondary)
                }

                Button(onClick = { onButtonSelect(1) },
                    modifier = Modifier.fillMaxWidth().padding(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                ) {
                    Text(text = "Search by Countries", color = MaterialTheme.colors.secondary)
                }

                Button(onClick = { onButtonSelect(2) },
                    modifier = Modifier.fillMaxWidth().padding(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                    ) {
                    Text(text = "Search by Languages", color = MaterialTheme.colors.secondary)
                }
            }


        }
    } else if(isLoading){
        FullScreenLoading()
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