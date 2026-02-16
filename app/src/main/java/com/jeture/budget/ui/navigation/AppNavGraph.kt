package com.jeture.budget.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jeture.budget.ui.screens.addedit.AddEditScreen
import com.jeture.budget.ui.screens.home.HomeScreen

@Composable
fun AppNavGraph(nav: NavHostController) {
    NavHost(navController = nav, startDestination = Routes.HOME) {
        composable(Routes.HOME) { HomeScreen(
            onAdd = { ym -> nav.navigate(Routes.add(ym)) }
            )
        }
        composable(Routes.ADD) { backStackEntry ->
            val ym = backStackEntry.arguments
                ?.getString("ym")
                ?: java.time.YearMonth.now().toString()

            AddEditScreen(
                initialYearMonth = ym,
                onDone = { nav.popBackStack() }
            )
        }
    }
}

