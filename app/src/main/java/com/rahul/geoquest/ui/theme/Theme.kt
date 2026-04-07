package com.rahul.geoquest.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.rahul.geoquest.domain.model.AppThemeMode

private val LightColors = lightColorScheme(
    primary = OceanBlue,
    onPrimary = CardSurface,
    primaryContainer = ColorPaletteLight.PrimaryContainer,
    onPrimaryContainer = DeepWater,
    secondary = AquaMint,
    onSecondary = DeepWater,
    secondaryContainer = ColorPaletteLight.SecondaryContainer,
    onSecondaryContainer = DeepWater,
    tertiary = GoldenSignal,
    onTertiary = DeepWater,
    tertiaryContainer = ColorPaletteLight.TertiaryContainer,
    onTertiaryContainer = DeepWater,
    background = Mist,
    onBackground = BodyCopy,
    surface = CardSurface,
    onSurface = BodyCopy,
    surfaceVariant = Cloud,
    onSurfaceVariant = BodyCopyMuted,
    outline = ColorPaletteLight.Outline,
    outlineVariant = ColorPaletteLight.OutlineVariant,
    error = DangerRose,
)

private val DarkColors = darkColorScheme(
    primary = OceanBlueDark,
    onPrimary = DeepWater,
    primaryContainer = ColorPaletteDark.PrimaryContainer,
    onPrimaryContainer = BodyCopyDark,
    secondary = AquaMintDark,
    onSecondary = DeepWater,
    secondaryContainer = ColorPaletteDark.SecondaryContainer,
    onSecondaryContainer = BodyCopyDark,
    tertiary = GoldenSignal,
    onTertiary = DeepWater,
    tertiaryContainer = ColorPaletteDark.TertiaryContainer,
    onTertiaryContainer = BodyCopyDark,
    background = NightSurface,
    onBackground = BodyCopyDark,
    surface = CardSurfaceDark,
    onSurface = BodyCopyDark,
    surfaceVariant = NightBackdrop,
    onSurfaceVariant = BodyCopyMutedDark,
    outline = NightOutline,
    outlineVariant = ColorPaletteDark.OutlineVariant,
    error = DangerRose,
)

@Composable
fun GeoQuestTheme(
    themeMode: AppThemeMode = AppThemeMode.System,
    content: @Composable () -> Unit,
) {
    val darkTheme = when (themeMode) {
        AppThemeMode.System -> isSystemInDarkTheme()
        AppThemeMode.Light -> false
        AppThemeMode.Dark -> true
    }

    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        shapes = GeoQuestShapes,
        content = content,
    )
}

private object ColorPaletteLight {
    val PrimaryContainer = Color(0xFFDCE9FF)
    val SecondaryContainer = Color(0xFFD9FAF2)
    val TertiaryContainer = Color(0xFFFFF1CB)
    val Outline = Color(0xFFD2DBE8)
    val OutlineVariant = Color(0xFFE1E9F4)
}

private object ColorPaletteDark {
    val PrimaryContainer = Color(0xFF16365F)
    val SecondaryContainer = Color(0xFF143B3A)
    val TertiaryContainer = Color(0xFF4A390D)
    val OutlineVariant = Color(0xFF223044)
}
