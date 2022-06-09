package com.google.samples.apps.nowinandroid.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.zIndex
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import com.google.samples.apps.nowinandroid.navigation.NiaNavHost
import com.google.samples.apps.nowinandroid.navigation.NiaTopLevelNavigation
import com.google.samples.apps.nowinandroid.playback.PlaybackMiniControls

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun NiaAppContent(
    windowSizeClass: WindowSizeClass,
    niaTopLevelNavigation: NiaTopLevelNavigation,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    Scaffold(
        modifier = Modifier,
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        bottomBar = {
            if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                Column {
                    PlaybackMiniControls(
                        modifier = Modifier
                            .graphicsLayer(translationY = 8.0F)
                            .zIndex(2f)
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