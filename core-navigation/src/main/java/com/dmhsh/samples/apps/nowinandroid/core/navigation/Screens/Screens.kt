package com.dmhsh.samples.apps.nowinandroid.core.navigation.Screens

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.core.net.toUri
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet

const val QUERY_KEY = "query"
const val SEARCH_BACKENDS_KEY = "backends"
//const val ARTIST_ID_KEY = "artist_id"
const val PLAYLIST_ID_KEY = "playlist_id"

interface Screen {
    val route: String
}

val ROOT_SCREENS = listOf(
    RootScreen.Search,
    RootScreen.Downloads,
    RootScreen.Library,
    RootScreen.Alarm,
    RootScreen.Settings
)

sealed class RootScreen(
    override val route: String,
    val startScreen: LeafScreen,
    val arguments: List<NamedNavArgument> = emptyList(),
    val deepLinks: List<NavDeepLink> = emptyList(),
) : Screen {
    object Search : RootScreen("search_root", LeafScreen.Search(rootRoute = "search_root"))
    object Downloads : RootScreen("downloads_root", LeafScreen.Downloads())
    object Library : RootScreen("library_root", LeafScreen.History())
    object Alarm : RootScreen("alarm_root", LeafScreen.Alarm())
    object Settings : RootScreen("settings_root", LeafScreen.Settings(rootRoute = "settings_root"))
}

sealed class LeafScreen(
    override val route: String,
    open val rootRoute: String? = null,
    protected open val path: String = "",
    val arguments: List<NamedNavArgument> = emptyList(),
    val deepLinks: List<NavDeepLink> = emptyList(),
) : Screen {

    fun createRoute(root: RootScreen? = null) =
        when (val rootPath = root?.route ?: this.rootRoute) {
            is String -> "$rootPath/$route"
            else -> route
        }

    data class Search(
        override val route: String = "search_root1",
        override val rootRoute: String = "search_root",
    ) : LeafScreen(
        route, rootRoute,
        arguments = listOf(
            navArgument(QUERY_KEY) {
                type = NavType.StringType
                nullable = true
            },
            navArgument(SEARCH_BACKENDS_KEY) {
                type = NavType.StringType
                nullable = true
            }
        ),
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "${"Config.BASE_URL"}search?$QUERY_KEY={$QUERY_KEY}&$SEARCH_BACKENDS_KEY={$SEARCH_BACKENDS_KEY}"
            }
        )
    ) {
        companion object {
            fun buildRoute(query: String) =
                "${RootScreen.Search.route}/search/?$QUERY_KEY=$query&$SEARCH_BACKENDS_KEY=#minerva"

            fun buildUri(query: String) = "${"Config.BASE_URL"}search?q=$query".toUri()
        }
    }

    data class Downloads(
        override val route: String = "downloads",
        override val rootRoute: String? = "downloads_root"
    ) : LeafScreen(route, rootRoute)

    data class Alarm(
        override val route: String = "alarm",
        override val rootRoute: String = "alarm_root"
    ) : LeafScreen(route, rootRoute)

    data class History(
        override val route: String = "library",
        override val rootRoute: String = "library_root"
    ) : LeafScreen(route, rootRoute)

    data class CreatePlaylist(
        override val route: String = "create_playlist",
        override val rootRoute: String = "library_root"
    ) : LeafScreen(route, rootRoute)

    data class PlaybackSheet(override val route: String = "playback_sheet") : LeafScreen(route)

    data class SearchingSheet(override val route: String = "search_sheet"): LeafScreen(route)

    data class Settings(
        override val route: String = "settings",
        override val rootRoute: String? = "settings_root"
    ) : LeafScreen(route, rootRoute)

}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.composableScreen(screen: LeafScreen, content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit) {
    composable(screen.createRoute(), screen.arguments, screen.deepLinks, content = content)
}

@OptIn(ExperimentalMaterialNavigationApi::class)
fun NavGraphBuilder.bottomSheetScreen(
    screen: LeafScreen,
    content: @Composable ColumnScope.(NavBackStackEntry) -> Unit
) =
    bottomSheet(screen.createRoute(), screen.arguments, screen.deepLinks, content)
