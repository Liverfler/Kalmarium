package com.example.kalmarium.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.material3.LocalTextStyle

// Fix világos, kontrasztos színvilág
private val AppColorScheme = lightColorScheme(
    primary = Color(0xFF12FF00),
    onPrimary = Color.White,

    secondary = Color(0xFF444444),
    onSecondary = Color.White,

    background = Color.White,
    onBackground = Color.Black,

    surface = Color.White,
    onSurface = Color.Black
)

@Composable
fun KalmariumTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = Typography,
        content = {
            CompositionLocalProvider(
                LocalTextStyle provides TextStyle(
                    color = Color.Black
                )
            ) {
                content()
            }
        }
    )
}
