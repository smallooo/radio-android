package com.dmhsh.samples.apps.nowinandroid.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy


//@Composable
//fun NiANavRail(
//    onNavigateToTopLevelDestination: (TopLevelDestination) -> Unit,
//    currentDestination: NavDestination?,
//    modifier: Modifier = Modifier,
//) {
//    NavigationRail(modifier = modifier) {
//        TOP_LEVEL_DESTINATIONS.forEach { destination ->
//            val selected =
//                currentDestination?.hierarchy?.any { it.route == destination.route } == true
//            NavigationRailItem(
//                selected = selected,
//                onClick = { onNavigateToTopLevelDestination(destination) },
//                icon = {
//                    Icon(
//                        if (selected) destination.selectedIcon else destination.unselectedIcon,
//                        contentDescription = null
//                    )
//                },
//                label = { Text(stringResource(destination.iconTextId)) }
//            )
//        }
//    }
//}