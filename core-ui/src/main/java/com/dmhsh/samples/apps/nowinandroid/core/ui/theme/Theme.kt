/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dmhsh.samples.apps.nowinandroid.core.ui.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

val DefaultTheme = ThemeState()
val DefaultThemeDark = ThemeState(DarkModePreference.ON)
val DefaultSpecs = Specs()

@Composable
fun RadioTheme(
    theme: ThemeState = DefaultTheme,
    changeSystemBar: Boolean = true,
    content: @Composable() () -> Unit
) {

    val isDarkTheme = when (theme.darkModePreference) {
        DarkModePreference.AUTO -> isSystemInDarkTheme()
        DarkModePreference.ON -> true
        DarkModePreference.OFF -> false
    }

    val colors = when (theme.colorPalettePreference) {
        ColorPalettePreference.Asphalt -> if (isDarkTheme) appDarkColors(Asphalt, Orange) else appLightColors(Asphalt, Orange)
        ColorPalettePreference.Black_Yellow -> {
            if (isDarkTheme) appDarkColors(Color.Black, Yellow, onSecondary = Color.Black)
            else appLightColors(Primary, Yellow500, onSecondary = Color.Black)
        }
        ColorPalettePreference.Gray -> if (isDarkTheme) appDarkColors(Gray1000, Secondary) else appLightColors(Gray1000, Secondary)
        ColorPalettePreference.Gray_Green -> if (isDarkTheme) appDarkColors(Gray1000, Green600) else appLightColors(Gray1000, Green600)
        ColorPalettePreference.Blue_Grey -> if (isDarkTheme) appDarkColors(BlueGrey, Green900) else appLightColors(BlueGrey, Green900)
        ColorPalettePreference.Black -> if (isDarkTheme) appDarkColors(Color.Black, Secondary) else appLightColors(Primary, Secondary)
        else -> if (isDarkTheme) DarkAppColors else LightAppColors
    }

    if (changeSystemBar) {
        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = colors.materialColors.isLight
            )
        }
    }

    ProvideAppTheme(theme, colors) {
        MaterialTheme(
            colors = animate(colors.materialColors),
            typography = Typography,
            content = content
        )
    }
}
