package com.google.samples.apps.nowinandroid.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import com.google.samples.apps.nowinandroid.common.compose.LocalPlaybackConnection
import com.google.samples.apps.nowinandroid.core.ui.theme.AppTheme
import com.google.samples.apps.nowinandroid.navigation.NiaNavHost
import com.google.samples.apps.nowinandroid.navigation.NiaTopLevelNavigation
import com.google.samples.apps.nowinandroid.playback.PlaybackConnection
import com.hdmsh.core_ui_playback.PlaybackMiniControls

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun NiaAppContent(
    windowSizeClass: WindowSizeClass,
    niaTopLevelNavigation: NiaTopLevelNavigation,
    currentDestination: NavDestination?,
    navController: NavHostController,
    playbackConnection: PlaybackConnection = LocalPlaybackConnection.current,
) {


    Scaffold(
        modifier = Modifier,
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        bottomBar = {
            if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                Column {
                    PlaybackMiniControls(
                        modifier = Modifier.padding(bottom = 8.dp),
                        contentPadding = PaddingValues(end = 16.dp),
                    )

                    NiABottomBar(
                        onNavigateToTopLevelDestination = niaTopLevelNavigation::navigateTo,
                        currentDestination = currentDestination
                    )
                }
            }
        }
    ) { padding ->
        Row(
            Modifier
                .fillMaxSize()
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal
                    )
                )
        ) {
            if (windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact) {
                NiANavRail(
                    onNavigateToTopLevelDestination = niaTopLevelNavigation::navigateTo,
                    currentDestination = currentDestination,
                    modifier = Modifier.safeDrawingPadding()
                )
            }

            NiaNavHost(
                windowSizeClass = windowSizeClass,
                navController = navController,
                modifier = Modifier
                    .padding(padding)
                    .consumedWindowInsets(padding)
            )
        }
    }
}