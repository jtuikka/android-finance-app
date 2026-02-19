package com.jeture.budget.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jeture.budget.ui.screens.addedit.AddEditScreen
import com.jeture.budget.ui.screens.charts.ChartsScreen
import com.jeture.budget.ui.screens.home.HomeScreen
import com.jeture.budget.ui.screens.settings.SettingsScreen

@Composable
fun AppNavGraph(
    nav: NavHostController,
    padding: PaddingValues = PaddingValues(0.dp)
) {
    NavHost(
        navController = nav,
        startDestination = BottomTab.Home.route,
        modifier = Modifier
    ) {
        composable(BottomTab.Home.route) {
            HomeScreen(
                onAdd = { ym -> nav.navigate("add/$ym") }
            )
        }

        composable(BottomTab.Charts.route) {
            ChartsScreen()
        }

        composable(BottomTab.Settings.route) {
            SettingsScreen()
        }

        composable("add/{ym}") { backStackEntry ->
            val ym = backStackEntry.arguments?.getString("ym")
                ?: java.time.YearMonth.now().toString()

            AddEditScreen(
                initialYearMonth = ym,
                onDone = { nav.popBackStack() }
            )
        }
    }
}
