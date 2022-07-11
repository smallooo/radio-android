package com.hdmsh.core_ui_playback.searching

import android.graphics.Bitmap
import androidx.palette.graphics.Palette

fun Bitmap.generateDominantColorState(): Palette.Swatch = Palette.Builder(this)
    .resizeBitmapArea(0)
    .maximumColorCount(16)
    .generate()
    .swatches
    .maxByOrNull { swatch -> swatch.population }!!
