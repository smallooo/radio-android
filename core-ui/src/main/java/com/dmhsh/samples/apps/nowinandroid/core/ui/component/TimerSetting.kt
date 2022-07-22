package com.dmhsh.samples.apps.nowinandroid.core.ui.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tm.alashow.i18n.R

@OptIn(ExperimentalMaterialApi::class, androidx.compose.animation.ExperimentalAnimationApi::class)
@Composable
fun SettingMenu(
    sliderState : Float,
    timeRemained: Int,
    modifier: Modifier,
    onValueChange: (Float) -> Unit,
    onTimerSet: (Long) -> Unit
) {



    //var settingValue by remember { mutableStateOf(0.toFloat()) }



    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(top = 68.dp)
                .animateContentSize(),
        ) {

            Text(modifier = Modifier.align(Alignment.CenterHorizontally),
                style = androidx.compose.material.MaterialTheme.typography.body1,
                text = stringResource(R.string.sleep_timer_title)
            )

            Text(modifier = Modifier.align(Alignment.CenterHorizontally),
                style = androidx.compose.material.MaterialTheme.typography.h2,
                text = sliderState.toInt().toString())

            Slider(value = sliderState,
                valueRange = 0f.. 600f,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                onValueChange = { newValue ->
                    onValueChange(newValue)
//                    settingValue = newValue
//                    sliderState = newValue
                },
                colors = SliderDefaults.colors(
                    activeTickColor = Color.Transparent,
                    inactiveTickColor = Color.Transparent,
                    inactiveTrackColor = Color.LightGray,
                    activeTrackColor = Color.Black,
                    thumbColor = Color.Black
            ))

            Row( modifier = Modifier.align(Alignment.End)) {
                NiaFilledButton(onClick = {
                    onTimerSet(0.toLong())
                }) {
                    Text(stringResource(R.string.action_cancel))
                }

                NiaFilledButton( modifier = Modifier.padding(horizontal = 8.dp),
                    onClick = {
                        onTimerSet(sliderState.toInt().toLong())
                    }) {
                    Text(stringResource(R.string.action_ok))
                }
            }
        }
    }
}