/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.dmhsh.samples.apps.nowinandroid.ui

import androidx.compose.animation.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.composable
import com.dmhsh.feature_history.HistoryRoute
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.navigation
import com.dmhsh.samples.apps.nowinandroid.core.navigation.LocalNavigator

import com.dmhsh.samples.apps.nowinandroid.core.navigation.NavigationEvent
import com.dmhsh.samples.apps.nowinandroid.core.navigation.Navigator
import com.dmhsh.samples.apps.nowinandroid.core.navigation.Screens.*
import com.dmhsh.samples.apps.nowinandroid.feature.author.SettingRoute


import com.dmhsh.samples.apps.nowinandroid.feature.foryou.ForYouScreen
import com.dmhsh.samples.apps.nowinandroid.feature.interests.InterestsRoute
import com.dmhsh.samples.apps.nowinandroid.feature.alarm.AlarmRoute
import com.hdmsh.common_compose.collectEvent
import com.hdmsh.core_ui_playback.PlaybackSheet

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    navigator: Navigator = LocalNavigator.current,
) {
    collectEvent(navigator.queue) { event ->
        when (event) {
            is NavigationEvent.Destination -> {
                if (navController.currentBackStackEntry?.destination?.route == LeafScreen.PlaybackSheet().route)
                    navController.navigateUp()
                event.root?.let {
                    navController.navigate(it) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
                navController.navigate(event.route)
            }
            is NavigationEvent.Back -> navController.navigateUp()
            else -> Unit
        }
    }
    AnimatedNavHost(
        navController = navController,
        startDestination = RootScreen.Search.route,
        modifier = modifier,
        enterTransition = { defaultEnterTransition(initialState, targetState) },
        exitTransition = { defaultExitTransition(initialState, targetState) },
        popEnterTransition = { defaultPopEnterTransition() },
        popExitTransition = { defaultPopExitTransition() },
    ) {
        addStationsRoot()
        addFavoritesRoot()
        addHistoryRoot()
        addAlarmRoot()
        addSettingsRoot()
        addPlaybackSheet()

    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addStationsRoot() {
    navigation(
        route = RootScreen.Search.route,
        startDestination = LeafScreen.Search().createRoute()
    ) {
        addSearch()
        addArtistDetails(RootScreen.Search)
        addAlbumDetails(RootScreen.Search)
        
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addFavoritesRoot() {
    navigation(
        route = RootScreen.Downloads.route,
        startDestination = LeafScreen.Downloads().createRoute()
    ) {
        addDownloads()
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addHistoryRoot() {
    navigation(
        route = RootScreen.Library.route,
        startDestination = LeafScreen.Library().createRoute()
    ) {
        addLibrary()
        addCreatePlaylist()
        addEditPlaylist()
        addPlaylistDetails(RootScreen.Library)
        addArtistDetails(RootScreen.Library)
        addAlbumDetails(RootScreen.Library)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addAlarmRoot() {
    navigation(
        route = RootScreen.Alarm.route,
        startDestination = LeafScreen.Alarm().createRoute()
    ) {
        addAlarm()
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addSettingsRoot() {
    navigation(
        route = RootScreen.Settings.route,
        startDestination = LeafScreen.Settings().createRoute()
    ) {
        addSettings()


    }
}

private fun NavGraphBuilder.addSearch() {
    composableScreen(LeafScreen.Search()) {
        Search()
    }
}

@Composable
fun Search(){
    ForYouScreen()
}

private fun NavGraphBuilder.addAlarm() {
    composableScreen(LeafScreen.Alarm()){
        AlarmRoute(onBackClick = { /*TODO*/ })
    }
}

private fun NavGraphBuilder.addSettings() {
    composableScreen(LeafScreen.Settings()){
        SettingRoute(onBackClick = { /*TODO*/ })
    }
}

private fun NavGraphBuilder.addDownloads() {
    composableScreen(LeafScreen.Downloads()) {
        Downloads()
    }
}

@Composable
fun Downloads(){
    InterestsRoute( )
}

private fun NavGraphBuilder.addLibrary() {
    composableScreen(LeafScreen.Library()) {
        History()
    }
}

@Composable
fun History(){
    HistoryRoute(onBackClick = { /*TODO*/ })
}



private fun NavGraphBuilder.addCreatePlaylist() {
    bottomSheetScreen(LeafScreen.CreatePlaylist()) {
        CreatePlaylist()
    }
}

@Composable
fun CreatePlaylist(){
    Text("hello6")
}

private fun NavGraphBuilder.addEditPlaylist() {
    bottomSheetScreen(EditPlaylistScreen()) {
        EditPlaylist()
    }
}

@Composable
fun EditPlaylist(){
    Text("hello7")
}

private fun NavGraphBuilder.addPlaylistDetails(root: RootScreen) {
    composable(LeafScreen.PlaylistDetail(rootRoute = root.route).route) {
        PlaylistDetail()
    }
}

@Composable
fun PlaylistDetail(){
    Text("hello8")
}

private fun NavGraphBuilder.addArtistDetails(root: RootScreen) {
    composable(LeafScreen.ArtistDetails(rootRoute = root.route).route) {
        ArtistDetail()
    }
}

@Composable
fun ArtistDetail(){
    Text("hello9")
}

private fun NavGraphBuilder.addAlbumDetails(root: RootScreen) {
    composable(LeafScreen.AlbumDetails(rootRoute = root.route).route) {
        AlbumDetail()
    }
}

@Composable
fun AlbumDetail(){
    Text("hello10")
}

private fun NavGraphBuilder.addPlaybackSheet() {
    bottomSheetScreen(LeafScreen.PlaybackSheet()) {
        PlaybackSheet()
    }
}

/**
 * Adds an [NavController.OnDestinationChangedListener] to this [NavController] and updates the
 * returned [State] which is updated as the destination changes.
 */
@Stable
@Composable
internal fun NavController.currentScreenAsState(): State<RootScreen> {
    val selectedItem = remember { mutableStateOf<RootScreen>(RootScreen.Search) }
    val rootScreens = listOf(RootScreen.Search, RootScreen.Downloads, RootScreen.Library, RootScreen.Alarm, RootScreen.Settings)
    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            rootScreens.firstOrNull { rs -> destination.hierarchy.any { it.route == rs.route } }?.let {
                selectedItem.value = it
            }
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return selectedItem
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultEnterTransition(
    initial: NavBackStackEntry,
    target: NavBackStackEntry,
): EnterTransition {
    val initialNavGraph = initial.destination.hostNavGraph
    val targetNavGraph = target.destination.hostNavGraph
    // If we're crossing nav graphs (bottom navigation graphs), we crossfade
    if (initialNavGraph.id != targetNavGraph.id || initial.destination.route == target.destination.route) {
        return fadeIn()
    }
    // Otherwise we're in the same nav graph, we can imply a direction
    return fadeIn() + slideIntoContainer(AnimatedContentScope.SlideDirection.Start)
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultExitTransition(
    initial: NavBackStackEntry,
    target: NavBackStackEntry,
): ExitTransition {
    val initialNavGraph = initial.destination.hostNavGraph
    val targetNavGraph = target.destination.hostNavGraph
    // If we're crossing nav graphs (bottom navigation graphs), we crossfade
    if (initialNavGraph.id != targetNavGraph.id || initial.destination.route == target.destination.route) {
        return fadeOut()
    }
    // Otherwise we're in the same nav graph, we can imply a direction
    return fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.Start)
}

internal val NavDestination.hostNavGraph: NavGraph
    get() = hierarchy.first { it is NavGraph } as NavGraph

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultPopEnterTransition(): EnterTransition {
    return fadeIn() + slideIntoContainer(AnimatedContentScope.SlideDirection.End)
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultPopExitTransition(): ExitTransition {
    return fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.End)
}
