/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.google.samples.apps.nowinandroid.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.google.samples.app.nowinandroid.core.playback.isActive
import com.google.samples.apps.nowinandroid.common.compose.LocalPlaybackConnection
import com.google.samples.apps.nowinandroid.core.navigation.Screens.RootScreen
import com.google.samples.apps.nowinandroid.core.ui.component.isWideLayout
import com.google.samples.apps.nowinandroid.playback.PlaybackConnection
import com.google.samples.apps.nowinandroid.ui.AppNavigation
import com.google.samples.apps.nowinandroid.ui.currentScreenAsState
import com.hdmsh.common_compose.rememberFlowWithLifecycle
import com.hdmsh.core_ui_playback.PlaybackMiniControls

val HomeBottomNavigationHeight = 56.dp

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
internal fun Home(
    windowSizeClass: WindowSizeClass,
    currentDestination: NavDestination?,
    navController: NavHostController,
    playbackConnection: PlaybackConnection = LocalPlaybackConnection.current,
) {
    val selectedTab by navController.currentScreenAsState()
    val playbackState by rememberFlowWithLifecycle(playbackConnection.playbackState)
    val nowPlaying by rememberFlowWithLifecycle(playbackConnection.nowPlaying)


    val isPlayerActive = (playbackState to nowPlaying).isActive
    val bottomBarHeight = HomeBottomNavigationHeight * (if (isPlayerActive) 1.15f else 1f)

    BoxWithConstraints {
        val isWideLayout = isWideLayout()
        val maxWidth = maxWidth
        Row(Modifier.fillMaxSize()) {
            if (isWideLayout)
                ResizableHomeNavigationRail(
                    maxWidth = maxWidth,
                    selectedTab = selectedTab,
                    navController = navController
                )
            Scaffold(
                modifier = Modifier,
                backgroundColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground,
                bottomBar = {
                    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                        Column {
                            PlaybackMiniControls(
                                modifier = Modifier.padding(bottom = 8.dp),
                                contentPadding = PaddingValues(end = 16.dp),
                            )
                            HomeBottomNavigation(
                                selectedTab = selectedTab,
                                onNavigationSelected = { selected -> navController.selectRootScreen(selected) },
                                playerActive = false,
                                modifier = Modifier.fillMaxWidth(),
                                height = bottomBarHeight
                            )
                        }
                    }
                }
            ) {
               AppNavigation(navController = navController)
            }
        }
    }
}

internal fun NavController.selectRootScreen(tab: RootScreen) {
    navigate(tab.route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
        val currentEntry = currentBackStackEntry
        val currentDestination = currentEntry?.destination
        val hostGraphRoute = ""
        val isReselected = hostGraphRoute == tab.route
        val isRootReselected = currentDestination?.route == tab.startScreen.createRoute()
        if (isReselected && !isRootReselected) {
            navigateUp()
        }
    }
}
