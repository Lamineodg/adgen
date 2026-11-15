package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AdStudioColorScheme = darkColorScheme(
    primary = AmberGold,
    onPrimary = Color(0xFF1E1300),
    primaryContainer = Color(0xFF3B2800),
    onPrimaryContainer = AmberGoldBright,
    
    secondary = ElectricViolet,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF331E54),
    onSecondaryContainer = ElectricVioletBright,
    
    tertiary = CyberCyan,
    onTertiary = Color(0xFF002026),
    tertiaryContainer = Color(0xFF003640),
    onTertiaryContainer = Color(0xFF70F3FF),
    
    background = StudioCanvas,
    onBackground = TextPrimary,
    
    surface = StudioSurface,
    onSurface = TextPrimary,
    surfaceVariant = StudioCard,
    onSurfaceVariant = TextSecondary,
    
    outline = StudioBorder,
    outlineVariant = Color(0xFF1E293B),
    
    error = BadgeRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AdStudioColorScheme,
        typography = Typography,
        content = content
    )
}
