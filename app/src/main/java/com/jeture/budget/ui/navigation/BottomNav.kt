package com.jeture.budget.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomTab(val route: String, val label: String, val icon: ImageVector) {
    data object Home : BottomTab("home", "Home", Icons.Filled.Home)
    data object Charts : BottomTab("charts", "Charts", Icons.Filled.DateRange)
    data object Settings : BottomTab("settings", "Settings", Icons.Filled.Settings)
}

val bottomTabs = listOf(BottomTab.Home, BottomTab.Charts, BottomTab.Settings)