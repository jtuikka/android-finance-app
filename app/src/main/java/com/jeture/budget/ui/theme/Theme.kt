package com.jeture.budget.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    secondary = Secondary,
    background = Background,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    surface = Surface,
    error = Error,
)
@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography,
        content = content
    )
}
