package com.jeture.budget.ui.screens.settings

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    Scaffold(topBar = { TopAppBar(title = { Text("Settings") }) }) { padding ->
        Text("Settings screen", modifier = androidx.compose.ui.Modifier.padding(padding))
    }
}
