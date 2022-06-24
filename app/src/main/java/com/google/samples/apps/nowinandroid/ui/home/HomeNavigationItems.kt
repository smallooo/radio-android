/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.google.samples.apps.nowinandroid.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import com.google.samples.apps.nowinandroid.R
import com.google.samples.apps.nowinandroid.core.navigation.Screens.RootScreen


internal val HomeNavigationItems = listOf(
    HomeNavigationItem.ImageVectorIcon(
        screen = RootScreen.Search,
        labelResId = com.google.samples.apps.nowinandroid.feature.foryou.R.string.nav_item_stations,
        contentDescriptionResId = R.string.app_id,
        iconImageVector = Icons.Outlined.Upcoming,
        selectedImageVector = Icons.Filled.Upcoming,
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = RootScreen.Downloads,
        labelResId = R.string.nav_item_starred,
        contentDescriptionResId = R.string.app_id,
        iconImageVector = Icons.Outlined.Grid3x3,
        selectedImageVector = Icons.Filled.Grid3x3,
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = RootScreen.Library,
        labelResId = com.google.samples.apps.nowinandroid.feature.foryou.R.string.nav_item_history,
        contentDescriptionResId = R.string.app_id,
        iconImageVector = Icons.Outlined.UTurnLeft,
        selectedImageVector = Icons.Filled.UTurnLeft,
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = RootScreen.Alarm,
        labelResId = R.string.nav_item_alarm,
        contentDescriptionResId = R.string.app_id,
        iconImageVector = Icons.Outlined.Gamepad,
        selectedImageVector = Icons.Filled.Gamepad,
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = RootScreen.Settings,
        labelResId = R.string.nav_item_settings,
        contentDescriptionResId = R.string.app_id,
        iconImageVector = Icons.Outlined.GTranslate,
        selectedImageVector =  Icons.Filled.GTranslate,
    ),
)
