package com.example.kalmarium.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun KalmariumTheme(
    themeType: AppThemeType,
    content: @Composable () -> Unit
) {
    val colors = when (themeType) {
        AppThemeType.BLUE -> BlueTheme
        AppThemeType.GREEN -> GreenTheme
        AppThemeType.PURPLE -> PurpleTheme
        AppThemeType.ORANGE -> OrangeTheme
        AppThemeType.RED -> RedTheme
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
