package com.jeture.budget.ui.navigation

import androidx.compose.material3.*
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.ListItemDefaults.contentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.navigation.NavGraph.Companion.findStartDestination

@Composable
fun RootScaffold(nav: NavHostController) {
    val navBackStackEntry by nav.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = bottomTabs.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    bottomTabs.forEach { tab ->
                        val selected = currentRoute == tab.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                nav.navigate(tab.route) {
                                    popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(tab.icon, contentDescription = tab.label) },
                            label = { Text(tab.label) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        AppNavGraph(nav = nav, padding = padding)
    }
}
