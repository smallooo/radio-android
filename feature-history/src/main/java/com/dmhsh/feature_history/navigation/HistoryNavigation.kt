package com.dmhsh.feature_history.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dmhsh.feature_history.HistoryRoute

import com.google.samples.apps.nowinandroid.core.navigation.NiaNavigationDestination


object HistoryDestination : NiaNavigationDestination {
    override val route = "history_route"
    override val destination = "author_destination"
    val historyIdArg = "historyId"
}

fun NavGraphBuilder.historyGraph(
    onBackClick: () -> Unit
) {
    composable(
        route = HistoryDestination.route
//        arguments = listOf(
//            navArgument(HistoryDestination.historyIdArg) {
//                type = NavType.StringType
//            }
//        )
    ) {
        HistoryRoute(onBackClick = onBackClick)
    }
}
