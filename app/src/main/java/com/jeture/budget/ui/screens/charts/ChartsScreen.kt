package com.jeture.budget.ui.screens.charts

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartsScreen() {
    Scaffold(topBar = { TopAppBar(title = { Text("Charts") }) }) { padding ->
        Text("Charts screen", modifier = androidx.compose.ui.Modifier.padding(padding))
    }
}
