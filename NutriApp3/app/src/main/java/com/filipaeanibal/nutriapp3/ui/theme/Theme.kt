package com.filipaeanibal.nutriapp3.ui.theme


import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.unit.dp

private val LightColors = lightColorScheme(
    primary = Color(0xFF4CAF50),
    onPrimary = Color.White,
    secondary = Color(0xFF388E3C),
    onSecondary = Color.White,
    background = Color(0xFFF5F5F5),
    surface = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF4CAF50),
    onPrimary = Color.Black,
    secondary = Color(0xFF388E3C),
    onSecondary = Color.Black,
    background = Color(0xFF121212),
    surface = Color(0xFF121212),
    onBackground = Color.White,
    onSurface = Color.White,
)

val AppShapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(16.dp)
)

@Composable
fun NutriAppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}
