package com.dmhsh.samples.apps.nowinandroid.core.ui.component

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tm.alashow.i18n.R

@OptIn(ExperimentalMaterialApi::class, androidx.compose.animation.ExperimentalAnimationApi::class)
@Composable
fun PalletMenu(
    timeRemained: Int,
    modifier: Modifier,
    onTimerSet: (Long) -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(top = 68.dp)
                .animateContentSize(),
        ) {
            var sliderState by remember { mutableStateOf(timeRemained.toFloat()) }
            //sliderState = timeRemained.toFloat()

//            Text(modifier = Modifier.align(Alignment.CenterHorizontally),
//                style = androidx.compose.material.MaterialTheme.typography.body1,
//                text = "close after" + timeRemained + "mins")

            Text(modifier = Modifier.align(Alignment.CenterHorizontally),
                style = androidx.compose.material.MaterialTheme.typography.body1,
                text = stringResource(R.string.sleep_timer_title)
            )

            Slider(value = sliderState, valueRange = 0f.. 600f,modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
                onValueChange = { newValue ->
                    sliderState = newValue
                    Log.e("aaa sliderState", sliderState.toInt().toString())
                }
            )

            Text(modifier = Modifier.align(Alignment.CenterHorizontally),
                style = androidx.compose.material.MaterialTheme.typography.h5,
                text = sliderState.toInt().toString())

            Row( modifier = Modifier.align(Alignment.End)) {
                Button(onClick = {
                    onTimerSet(0.toLong())
                }) {
                    Text(stringResource(R.string.action_cancel))
                }

                Button( modifier = Modifier.padding(horizontal = 8.dp),
                    onClick = {
                        onTimerSet(sliderState.toInt().toLong())
                    }) {
                    Text(stringResource(R.string.action_ok))
                }
            }

        }
    }
}