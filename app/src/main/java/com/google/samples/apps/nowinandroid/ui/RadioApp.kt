package com.google.samples.apps.nowinandroid.ui

import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.samples.apps.nowinandroid.core.ui.component.NiaBackground
import com.google.samples.apps.nowinandroid.core.ui.theme.NiaTheme
import com.google.samples.apps.nowinandroid.navigation.NiaTopLevelNavigation
import com.google.samples.apps.nowinandroid.playback.LocalScaffoldState

@Composable
fun RadioApp(
    windowSizeClass: WindowSizeClass,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val navController = rememberNavController()
    val niaTopLevelNavigation =
        remember(navController) { NiaTopLevelNavigation(navController) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    RadioCore(scaffoldState, windowSizeClass) {
        NiaAppContent(
            windowSizeClass,
            niaTopLevelNavigation,
            currentDestination,
            navController
        )
    }
}

@Composable
private fun RadioCore(
    scaffoldState: ScaffoldState,
    windowSizeClass: WindowSizeClass,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalScaffoldState provides scaffoldState) {
        NiaTheme {
            NiaBackground {
                content()
            }
        }
    }
}

