package com.google.samples.apps.nowinandroid.ui

import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.samples.apps.nowinandroid.common.compose.LocalPlaybackConnection
import com.google.samples.apps.nowinandroid.common.compose.LocalScaffoldState

import com.google.samples.apps.nowinandroid.core.ui.component.NiaBackground
import com.google.samples.apps.nowinandroid.core.ui.media.radioStations.LocalAudioActionHandler
import com.google.samples.apps.nowinandroid.core.ui.media.radioStations.audioActionHandler

import com.google.samples.apps.nowinandroid.core.ui.theme.NiaTheme
import com.google.samples.apps.nowinandroid.navigation.NiaTopLevelNavigation
import com.hdmsh.core_ui_playback.PlaybackConnectionViewModel


@Composable
fun RadioApp(
    windowSizeClass: WindowSizeClass,
) {
    val navController = rememberNavController()
    val niaTopLevelNavigation = remember(navController) { NiaTopLevelNavigation(navController) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    RadioCore( windowSizeClass) {
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
    windowSizeClass: WindowSizeClass,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    content: @Composable () -> Unit
) {

    CompositionLocalProvider(LocalScaffoldState provides scaffoldState) {
            NiaTheme {
               NiaBackground {
                    PlaybackHost {
                        RadioActionHandlers {
                            content()
                        }
                    }
                }
            }

    }
}

@Composable
fun PlaybackHost(
    viewModel: PlaybackConnectionViewModel = hiltViewModel(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalPlaybackConnection provides viewModel.playbackConnection,
    ) {
        content()
    }
}

@Composable
private fun RadioActionHandlers(content: @Composable () -> Unit) {
    val audioActionHandler = audioActionHandler()
        CompositionLocalProvider(
            LocalAudioActionHandler provides audioActionHandler,
        ) {
            content()
        }
    }


