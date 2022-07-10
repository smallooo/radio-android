package com.dmhsh.samples.apps.nowinandroid.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.plusAssign
import com.dmhsh.samples.apps.nowinandroid.BuildConfig
import com.dmhsh.samples.apps.nowinandroid.ui.home.Home
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.dmhsh.samples.apps.nowinandroid.common.compose.LocalPlaybackConnection
import com.dmhsh.samples.apps.nowinandroid.common.compose.LocalScaffoldState
import com.dmhsh.samples.apps.nowinandroid.core.navigation.NavigatorHost
import com.dmhsh.samples.apps.nowinandroid.core.navigation.rememberBottomSheetNavigator

import com.dmhsh.samples.apps.nowinandroid.core.ui.component.NiaBackground
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.rememberFlowWithLifecycle
import com.dmhsh.samples.apps.nowinandroid.core.ui.media.radioStations.LocalAudioActionHandler
import com.dmhsh.samples.apps.nowinandroid.core.ui.media.radioStations.audioActionHandler

import com.dmhsh.samples.apps.nowinandroid.core.ui.theme.RadioTheme
import com.dmhsh.samples.apps.nowinandroid.core.ui.theme.ThemeViewModel
import com.dmhsh.samples.apps.nowinandroid.feature.author.LocalAppVersion
import com.google.firebase.analytics.FirebaseAnalytics


import com.hdmsh.core_ui_playback.PlaybackConnectionViewModel
import kotlinx.coroutines.InternalCoroutinesApi


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialNavigationApi::class, InternalCoroutinesApi::class,
    ExperimentalAnimationApi::class)
@Composable
fun RadioApp(
    windowSizeClass: WindowSizeClass,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberAnimatedNavController(),
    analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(LocalContext.current),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    CompositionLocalProvider(
        LocalScaffoldState provides scaffoldState,
       // LocalAnalytics provides analytics,
        LocalAppVersion provides BuildConfig.VERSION_NAME
    ) {
        ProvideWindowInsets(consumeWindowInsets = false) {
            RadioCore(windowSizeClass) {
                val bottomSheetNavigator = rememberBottomSheetNavigator()
                navController.navigatorProvider += bottomSheetNavigator
                ModalBottomSheetLayout(bottomSheetNavigator) {
                    Home(windowSizeClass, currentDestination, navController)
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
    val themeState by rememberFlowWithLifecycle( themeViewModel.themeState)
    CompositionLocalProvider(LocalScaffoldState provides scaffoldState) {
            RadioTheme(themeState) {
                NavigatorHost {
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


