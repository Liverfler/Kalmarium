package com.example.kalmarium.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

val Typography.blackGlow: TextStyle
    get() = bodyLarge.copy(
        shadow = Shadow(
            color = Color.Black,
            offset = Offset(8f, 8f),
            blurRadius = 3f
        )
    )

@Composable
fun Typography.solidGlow(): TextStyle {
    return bodyLarge.copy(
        color = Color(0xFF000000),
        shadow = Shadow(
            color = MaterialTheme.colorScheme.primary,
            offset = Offset(2f, 2f),
            blurRadius = 2f
        )
    )
}