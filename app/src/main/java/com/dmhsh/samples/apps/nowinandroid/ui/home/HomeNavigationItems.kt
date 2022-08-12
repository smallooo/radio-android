/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.dmhsh.samples.apps.nowinandroid.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*



import com.dmhsh.samples.apps.nowinandroid.core.navigation.Screens.RootScreen


internal val HomeNavigationItems = listOf(
    HomeNavigationItem.ImageVectorIcon(
        screen = RootScreen.Search,
        labelResId = com.dmhsh.samples.apps.nowinandroid.feature.foryou.R.string.nav_item_stations,
        contentDescriptionResId = com.dmhsh.samples.apps.nowinandroid.feature.foryou.R.string.nav_item_stations,
        iconImageVector = Icons.Outlined.Upcoming,
        selectedImageVector = Icons.Filled.Upcoming,
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = RootScreen.Library,
        labelResId = com.dmhsh.samples.apps.nowinandroid.feature.foryou.R.string.nav_item_history,
        contentDescriptionResId = com.dmhsh.samples.apps.nowinandroid.feature.foryou.R.string.nav_item_history,
        iconImageVector = Icons.Outlined.UTurnLeft,
        selectedImageVector = Icons.Filled.UTurnLeft,
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = RootScreen.Alarm,
        labelResId = com.dmhsh.samples.apps.nowinandroid.feature.foryou.R.string.action_search,
        contentDescriptionResId = com.dmhsh.samples.apps.nowinandroid.feature.foryou.R.string.action_search,
        iconImageVector = Icons.Outlined.Search,
        selectedImageVector = Icons.Filled.Search,
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = RootScreen.Downloads,
        labelResId = com.dmhsh.samples.apps.nowinandroid.feature.foryou.R.string.nav_item_starred,
        contentDescriptionResId = com.dmhsh.samples.apps.nowinandroid.feature.foryou.R.string.nav_item_stations,
        iconImageVector = Icons.Outlined.Grid3x3,
        selectedImageVector = Icons.Filled.Grid3x3,
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = RootScreen.Settings,
        labelResId = com.dmhsh.samples.apps.nowinandroid.feature.foryou.R.string.nav_item_settings,
        contentDescriptionResId = com.dmhsh.samples.apps.nowinandroid.feature.foryou.R.string.nav_item_settings,
        iconImageVector = Icons.Outlined.Settings,
        selectedImageVector =  Icons.Filled.Settings,
    ),
)
