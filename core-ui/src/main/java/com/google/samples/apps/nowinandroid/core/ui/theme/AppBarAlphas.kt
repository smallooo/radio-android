/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.google.samples.apps.nowinandroid.core.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

object AppBarAlphas {
    @Composable
    fun translucentBarAlpha(): Float = when {
        MaterialTheme.colors.isLight -> 0.97f
        else -> 0.95f
    }
}
