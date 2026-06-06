package com.example.habisin.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.Color

/**
 * Extended brand colors that Material3's [androidx.compose.material3.ColorScheme] can't express
 * (the bottom nav, lime/peach accent cards, fields, dividers). These swap between the light and
 * the "Verdant Dark" palette so screens never hardcode a hex and dark mode stays consistent.
 *
 * Rule of thumb for screens:
 *  - tappable thing (button, link, CTA, FAB icon tint) → [action]
 *  - page/card background → MaterialTheme.colorScheme.background / .surface
 *  - text → MaterialTheme.colorScheme.onBackground / .onSurface (use [textMuted] for secondary)
 *  - brand accent surfaces (nav, stat cards, attention card) → the named tokens below
 */
@Immutable
data class HabisinColors(
    val action: Color,             // the ONE interactive color (coral)
    val onAction: Color,
    val textMuted: Color,
    val navBar: Color,
    val onNavBar: Color,           // unselected nav icon
    val navSelected: Color,        // selected nav icon
    val fab: Color,
    val onFab: Color,
    val limeCard: Color,           // brand card surface (light: lime / dark: neutral elevated)
    val onLimeCard: Color,
    val peachCard: Color,          // account card (light: peach / dark: neutral elevated)
    val onPeachCard: Color,
    val selectedContainer: Color,  // selected/active state (chips, theme pick, category)
    val onSelectedContainer: Color,
    val fieldBg: Color,
    val fieldHint: Color,
    val divider: Color,
)

private val LightHabisinColors = HabisinColors(
    action              = HabisinCoral,
    onAction            = HabisinWhite,
    textMuted           = HabisinTextMuted,
    navBar              = HabisinOlive,
    onNavBar            = HabisinWhite,
    navSelected         = HabisinPeach,
    fab                 = HabisinLime,
    onFab               = HabisinOlive,
    limeCard            = HabisinLime,
    onLimeCard          = HabisinOlive,
    peachCard           = HabisinPeach,
    onPeachCard         = HabisinTextDark,
    selectedContainer   = HabisinLime,    // light keeps the brand lime highlight
    onSelectedContainer = HabisinOlive,
    fieldBg             = HabisinLightGray,
    fieldHint           = HabisinTextMuted,
    divider             = DividerGray,
)

private val DarkHabisinColors = HabisinColors(
    action              = HabisinCoral,
    onAction            = HabisinWhite,
    textMuted           = HabisinDarkMuted,
    navBar              = HabisinDarkNav,         // the only green in dark
    onNavBar            = HabisinDarkText,
    navSelected         = HabisinCoral,           // selected nav = coral accent
    fab                 = HabisinDarkFab,
    onFab               = HabisinDarkFabIcon,
    limeCard            = HabisinDarkSurface2,     // neutral, not olive
    onLimeCard          = HabisinDarkText,
    peachCard           = HabisinDarkSurface2,     // neutral, not brown
    onPeachCard         = HabisinDarkText,
    selectedContainer   = HabisinDarkSelected,     // coral-tinted = visible selection
    onSelectedContainer = HabisinDarkOnSelected,
    fieldBg             = HabisinDarkSurface2,
    fieldHint           = HabisinDarkMuted,
    divider             = HabisinDarkDivider,
)

private val LocalHabisinColors = staticCompositionLocalOf { LightHabisinColors }

/** Accessor: `HabisinTheme.colors.action`, etc. */
object HabisinTheme {
    val colors: HabisinColors
        @Composable @ReadOnlyComposable
        get() = LocalHabisinColors.current
}

//Light scheme — coral is the primary (interactive) color; olive/teal are brand support.
private val HabisinLightColors = lightColorScheme(
    primary          = HabisinCoral,
    onPrimary        = HabisinWhite,
    secondary        = HabisinOlive,
    onSecondary      = HabisinWhite,
    tertiary         = HabisinTeal,
    onTertiary       = HabisinWhite,
    background       = HabisinWhite,
    onBackground     = HabisinTextDark,
    surface          = HabisinWhite,
    onSurface        = HabisinTextDark,
    surfaceVariant   = HabisinLightGray,
    onSurfaceVariant = HabisinTextMuted,
    outline          = DividerGray,
)

//Dark scheme — "Verdant Dark". Opaque surfaces, coral stays the interactive color.
private val HabisinDarkColors = darkColorScheme(
    primary          = HabisinCoral,
    onPrimary        = HabisinWhite,
    secondary        = HabisinLime,
    onSecondary      = HabisinOlive,
    tertiary         = HabisinTeal,
    onTertiary       = HabisinWhite,
    background       = HabisinDarkBg,
    onBackground     = HabisinDarkText,
    surface          = HabisinDarkSurface,
    onSurface        = HabisinDarkText,
    surfaceVariant   = HabisinDarkSurface2,
    onSurfaceVariant = HabisinDarkMuted,
    outline          = HabisinDarkDivider,
)

@Composable
fun HabisInTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic (Material You) color is intentionally OFF — the brand palette must stay consistent.
    content: @Composable () -> Unit
) {
    val colorScheme    = if (darkTheme) HabisinDarkColors else HabisinLightColors
    val habisinColors  = if (darkTheme) DarkHabisinColors else LightHabisinColors

    CompositionLocalProvider(LocalHabisinColors provides habisinColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography  = Typography,
            content     = content
        )
    }
}
