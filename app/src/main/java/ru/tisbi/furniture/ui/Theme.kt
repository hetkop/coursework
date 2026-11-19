package ru.tisbi.furniture.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Navy = Color(0xFF1B3A5C)
val Blue = Color(0xFF2E75B6)
val Danger = Color(0xFFC0392B)
val Muted = Color(0xFF6B7785)
val LowStockBg = Color(0xFFFFCCCC)

private val Colors = lightColorScheme(
    primary = Navy, onPrimary = Color.White,
    primaryContainer = Color(0xFFD0E4F7), onPrimaryContainer = Navy,
    secondary = Blue, onSecondary = Color.White,
    error = Danger, onError = Color.White,
    background = Color(0xFFF3F5F8), onBackground = Color(0xFF1C1B1F),
    surface = Color.White, onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFE8EEF4), outline = Color(0xFFCCCCCC)
)

@Composable
fun FurnitureTheme(content: @Composable () -> Unit) = MaterialTheme(colorScheme = Colors, content = content)
