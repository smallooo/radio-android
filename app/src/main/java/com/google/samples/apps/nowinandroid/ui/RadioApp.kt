package com.google.samples.apps.nowinandroid.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.input.key.Key.Companion.Home
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.plusAssign
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.samples.apps.nowinandroid.common.compose.LocalPlaybackConnection
import com.google.samples.apps.nowinandroid.common.compose.LocalScaffoldState
import com.google.samples.apps.nowinandroid.core.navigation.NavigatorHost
import com.google.samples.apps.nowinandroid.core.navigation.rememberBottomSheetNavigator

import com.google.samples.apps.nowinandroid.core.ui.component.NiaBackground
import com.google.samples.apps.nowinandroid.core.ui.media.radioStations.LocalAudioActionHandler
import com.google.samples.apps.nowinandroid.core.ui.media.radioStations.audioActionHandler
import com.google.samples.apps.nowinandroid.core.ui.theme.AppTheme
import com.google.samples.apps.nowinandroid.core.ui.theme.DefaultTheme

import com.google.samples.apps.nowinandroid.core.ui.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.ui.theme.ThemeViewModel
import com.google.samples.apps.nowinandroid.navigation.NiaTopLevelNavigation
import com.google.samples.apps.nowinandroid.ui.home.Home
import com.hdmsh.common_compose.rememberFlowWithLifecycle
import com.hdmsh.core_ui_playback.PlaybackConnectionViewModel
import kotlinx.coroutines.InternalCoroutinesApi


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialNavigationApi::class, InternalCoroutinesApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun RadioApp(
    windowSizeClass: WindowSizeClass,
    navController: NavHostController = rememberAnimatedNavController(),
) {
    val niaTopLevelNavigation = remember(navController) { NiaTopLevelNavigation(navController) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    CompositionLocalProvider() {
        ProvideWindowInsets(consumeWindowInsets = false) {
            RadioCore(windowSizeClass) {
                val bottomSheetNavigator = rememberBottomSheetNavigator()
                navController.navigatorProvider += bottomSheetNavigator
                ModalBottomSheetLayout(bottomSheetNavigator) {
                    Home(windowSizeClass, niaTopLevelNavigation, currentDestination, navController)
                }
            }
        }
    }
}

@Composable
private fun RadioCore(
    windowSizeClass: WindowSizeClass,
    themeViewModel: ThemeViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    content: @Composable () -> Unit
) {
    val themeState = DefaultTheme
    CompositionLocalProvider(LocalScaffoldState provides scaffoldState) {
        NiaTheme {
           // AppTheme(themeState) {
                NavigatorHost {
                    NiaBackground {
                        PlaybackHost {
                            RadioActionHandlers {
                                content()
                            }
                        }
                    }
                }
        //    }
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


