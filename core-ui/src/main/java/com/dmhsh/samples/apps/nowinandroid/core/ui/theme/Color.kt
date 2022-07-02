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

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.isUnspecified
import androidx.core.graphics.ColorUtils
import kotlin.math.roundToInt
import kotlin.random.Random

/**
 * Now in Android colors.
 */
val Blue10 = Color(0xFF001F29)
val Blue20 = Color(0xFF003544)
val Blue30 = Color(0xFF004D61)
val Blue40 = Color(0xFF006781)
val Blue80 = Color(0xFF5DD4FB)
val Blue90 = Color(0xFFB5EAFF)
val Blue95 = Color(0xFFDCF5FF)
val DarkGreen10 = Color(0xFF0D1F12)
val DarkGreen20 = Color(0xFF223526)
val DarkGreen30 = Color(0xFF394B3C)
val DarkGreen40 = Color(0xFF4F6352)
val DarkGreen80 = Color(0xFFB7CCB8)
val DarkGreen90 = Color(0xFFD3E8D3)
val DarkGreenGray10 = Color(0xFF1A1C1A)
val DarkGreenGray90 = Color(0xFFE2E3DE)
val DarkGreenGray95 = Color(0xFFF0F1EC)
val DarkGreenGray99 = Color(0xFFFBFDF7)
val DarkPurpleGray10 = Color(0xFF201A1B)
val DarkPurpleGray90 = Color(0xFFECDFE0)
val DarkPurpleGray95 = Color(0xFFFAEEEF)
val DarkPurpleGray99 = Color(0xFFFCFCFC)
val Green10 = Color(0xFF00210B)
val Green20 = Color(0xFF003919)
val Green30 = Color(0xFF005227)
val Green40 = Color(0xFF006D36)
val Green80 = Color(0xFF0EE37C)
val Green90 = Color(0xFF5AFF9D)
val GreenGray30 = Color(0xFF414941)
val GreenGray50 = Color(0xFF727971)
val GreenGray60 = Color(0xFF8B938A)
val GreenGray80 = Color(0xFFC1C9BF)
val GreenGray90 = Color(0xFFDDE5DB)
val Orange10 = Color(0xFF390C00)
val Orange20 = Color(0xFF5D1900)
val Orange30 = Color(0xFF812800)
val Orange40 = Color(0xFFA23F16)
val Orange80 = Color(0xFFFFB599)
val Orange90 = Color(0xFFFFDBCE)
val Orange95 = Color(0xFFFFEDE6)
val Purple10 = Color(0xFF36003D)
val Purple20 = Color(0xFF560A5E)
val Purple30 = Color(0xFF702776)
val Purple40 = Color(0xFF8C4190)
val Purple80 = Color(0xFFFFA8FF)
val Purple90 = Color(0xFFFFD5FC)
val Purple95 = Color(0xFFFFEBFB)
val PurpleGray30 = Color(0xFF4E444C)
val PurpleGray50 = Color(0xFF7F747C)
val PurpleGray60 = Color(0xFF998D96)
val PurpleGray80 = Color(0xFFD0C2CC)
val PurpleGray90 = Color(0xFFEDDEE8)
val Red10 = Color(0xFF410001)
val Red20 = Color(0xFF680003)
val Red30 = Color(0xFF930006)
val Red40 = Color(0xFFBA1B1B)
val Red80 = Color(0xFFFFB4A9)
val Red90 = Color(0xFFFFDAD4)
val Teal10 = Color(0xFF001F26)
val Teal20 = Color(0xFF02363F)
val Teal30 = Color(0xFF214D56)
val Teal40 = Color(0xFF3A656F)
val Teal80 = Color(0xFFA2CED9)
val Teal90 = Color(0xFFBEEAF6)

/**
 * Lighten the current [Color] instance to the given [luminance].
 *
 * This is needed because we can't access the token values directly. For the dynamic color theme,
 * this makes it impossible to get the 95% luminance token of the different theme colors.
 * TODO: Link to bug
 */
internal fun Color.lighten(luminance: Float): Color {
    val hsl = FloatArray(3)
    ColorUtils.RGBToHSL(
        (red * 256).roundToInt(),
        (green * 256).roundToInt(),
        (blue * 256).roundToInt(),
        hsl
    )
    hsl[2] = luminance
    val color = Color(ColorUtils.HSLToColor(hsl))
    return color
}

fun parseColor(hexColor: String) = Color(android.graphics.Color.parseColor(hexColor))
fun Int.toColor() = Color(this)

val Primary = Color(0xFF16053D)
val PrimaryVariant = Color(0xFF221652)
val Secondary = Color(0xFFFF3C8F)
val SecondaryVariant = Color(0xFFef0076)

val WhiteTransparent = Color(0x80FFFFFF)

val Red = Color(0xFFFF3B30)
val Red700 = Color(0xFFC0392b)
val Orange = Color(0xFFFF9500)
val Yellow = Color(0xFFFFCC00)
val Yellow500 = Color(0xFFFBBC04)
val Green = Color(0xFF4CD964)
val Blue300 = Color(0xFF5AC8FA)
val Blue = Color(0xFF007AFF)
val Purple = Color(0xFF5856D6)
val Asphalt = Color(0xFF2c3e50)

val Gray1000 = Color(0xFF121212)
val BlueGrey = Color(0xFF263238)
val Green600 = Color(0xFF1DB954)
val Green900 = Color(0xFF468847)

internal val DarkAppColors = appDarkColors(Primary, Secondary, PrimaryVariant, SecondaryVariant)
internal val LightAppColors = appLightColors(Primary, Secondary, PrimaryVariant, SecondaryVariant)

fun appDarkColors(
    primary: Color,
    secondary: Color,
    primaryVariant: Color = primary,
    secondaryVariant: Color = secondary,
    background: Color = primary,
    surface: Color = primary,
    onPrimary: Color = Color.White,
    onSecondary: Color = Color.White,
    onSurface: Color = Color.White,
    onSurfaceInputBackground: Color = Color(0x45706d86),
) = AppColors(
    _onSurfaceInputBackground = onSurfaceInputBackground,
    _materialColors = darkColors(
        primary = primary,
        onPrimary = onPrimary,
        primaryVariant = primaryVariant,
        secondary = secondary,
        onSecondary = onSecondary,
        secondaryVariant = secondaryVariant,
        background = background,
        surface = surface,
        onSurface = onSurface,
    )
)

fun appLightColors(
    primary: Color,
    secondary: Color,
    primaryVariant: Color = primary,
    secondaryVariant: Color = secondary,
    background: Color = Color.White,
    surface: Color = Color.White,
    onPrimary: Color = Color.White,
    onSecondary: Color = Color.White,
    onSurface: Color = Color.Black,
    onSurfaceInputBackground: Color = Color(0x45c1bbc0),
) = AppColors(
    _onSurfaceInputBackground = onSurfaceInputBackground,
    _materialColors = lightColors(
        primary = primary,
        onPrimary = onPrimary,
        primaryVariant = primaryVariant,
        secondary = secondary,
        onSecondary = onSecondary,
        secondaryVariant = secondaryVariant,
        background = background,
        surface = surface,
        onSurface = onSurface,
    )
)

@Composable
fun Colors.plainSurfaceColor() = if (MaterialTheme.colors.isLight) Color.White else Color.Black

@Composable
fun Colors.plainBackgroundColor() = if (!MaterialTheme.colors.isLight) Color.White else Color.Black

@Composable
fun Colors.plainGrayBackground() = if (MaterialTheme.colors.isLight) Color.LightGray else Color.DarkGray

@Composable
fun Color.disabledAlpha(condition: Boolean): Color = copy(alpha = if (condition) alpha else ContentAlpha.disabled)

@Composable
fun Color.contrastComposite(alpha: Float = 0.1f) = contentColorFor(this).copy(alpha = alpha).compositeOver(this)

@Composable
internal fun animate(colors: Colors): Colors {
    val animationSpec = remember { spring<Color>() }

    @Composable
    fun animateColor(color: Color): Color = animateColorAsState(targetValue = color, animationSpec = animationSpec).value

    return Colors(
        primary = animateColor(colors.primary),
        primaryVariant = animateColor(colors.primaryVariant),
        secondary = animateColor(colors.secondary),
        secondaryVariant = animateColor(colors.secondaryVariant),
        background = animateColor(colors.background),
        surface = animateColor(colors.surface),
        error = animateColor(colors.error),
        onPrimary = animateColor(colors.onPrimary),
        onSecondary = animateColor(colors.onSecondary),
        onBackground = animateColor(colors.onBackground),
        onSurface = animateColor(colors.onSurface),
        onError = animateColor(colors.onError),
        isLight = colors.isLight,
    )
}

@Composable
fun translucentSurfaceColor() = MaterialTheme.colors.surface.copy(alpha = AppBarAlphas.translucentBarAlpha())

//fun Modifier.translucentSurface() = composed { background(translucentSurfaceColor()) }

@Composable
fun Modifier.randomBackground(memoize: Boolean = true) = background(if (memoize) remember { randomColor() } else randomColor())

fun randomColor() = Color(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))

fun Color.fallbackTo(color: Color): Color = if (isUnspecified) color else this
