package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

// Let's configure light color scheme to match user's custom editorial blush-berry aesthetic
private val SpedexLightColorScheme = lightColorScheme(
    primary = TealAccent, // Berry accent color
    secondary = IndigoAccent,
    tertiary = NeonEmerald,
    background = SpaceBlack, // Soft off-pink
    surface = SpaceCard, // White
    onPrimary = Color.White,
    onSecondary = SpaceTextPrimary,
    onTertiary = Color.White,
    onBackground = SpaceTextPrimary,
    onSurface = SpaceTextPrimary
)

// Define both to be consistent, so no matter the default system theme, they see the gorgeous light berry theme
private val SpedexDarkColorScheme = lightColorScheme(
    primary = TealAccent,
    secondary = IndigoAccent,
    tertiary = NeonEmerald,
    background = SpaceBlack,
    surface = SpaceCard,
    onPrimary = Color.White,
    onSecondary = SpaceTextPrimary,
    onTertiary = Color.White,
    onBackground = SpaceTextPrimary,
    onSurface = SpaceTextPrimary
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false, // Set default to false to render the gorgeous light theme directly
    dynamicColor: Boolean = false, // Disable dynamic colors to preserve customized brand identity
    content: @Composable () -> Unit,
) {
    // Force the custom Light Color Scheme because it perfectly matches the beautiful editorial blush/berry design
    val colorScheme = SpedexLightColorScheme

    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
