/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.google.samples.apps.nowinandroid.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import com.google.samples.apps.nowinandroid.R
import com.google.samples.apps.nowinandroid.core.navigation.Screens.RootScreen


internal val HomeNavigationItems = listOf(
    HomeNavigationItem.ImageVectorIcon(
        screen = RootScreen.Search,
        labelResId = R.string.app_id,
        contentDescriptionResId = R.string.app_id,
        iconImageVector = Icons.Outlined.Search,
        selectedImageVector = Icons.Filled.Search,
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = RootScreen.Downloads,
        labelResId = R.string.app_id,
        contentDescriptionResId = R.string.app_id,
        iconImageVector = Icons.Outlined.Download,
        selectedImageVector = Icons.Filled.Download,
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = RootScreen.Library,
        labelResId = R.string.app_id,
        contentDescriptionResId = R.string.app_id,
        iconImageVector = Icons.Outlined.LibraryMusic,
        selectedImageVector = Icons.Filled.LibraryMusic,
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = RootScreen.Settings,
        labelResId = R.string.app_id,
        contentDescriptionResId = R.string.app_id,
        iconImageVector = Icons.Outlined.Settings,
        selectedImageVector = Icons.Filled.Settings,
    ),
)
