package com.assignment.chessboard.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
    primary = Color(0xFFBB86FC),
    tertiary = Color(0xFF3700B3),
    secondary = Color(0xFF03DAC5)
)

private val LightColorPalette = lightColorScheme(
    primary = Color(0xFF6200EE),
    tertiary = Color(0xFF3700B3),
    secondary = Color(0xFF03DAC5)
)

@Composable
fun ChessboardScreenTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val shapes = Shapes()

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = shapes,
        content = content
    )
}
