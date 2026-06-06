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
    val attentionCard: Color,      // home "attention required" block (alert)
    val onAttentionCard: Color,
    val attentionRow: Color,       // the item rows inside that block
    val fieldBg: Color,
    val fieldHint: Color,
    val divider: Color,
)

// Minimal & cohesive: neutral surfaces everywhere, coral only as the action accent,
// green only on the nav bar. Selected states are a hueless inverted neutral.
private val LightHabisinColors = HabisinColors(
    action              = HabisinCoral,
    onAction            = HabisinWhite,
    textMuted           = HabisinTextMuted,
    navBar              = HabisinOlive,
    onNavBar            = HabisinWhite.copy(alpha = 0.55f),  // dimmed unselected
    navSelected         = HabisinWhite,                      // bright selected, no coral
    fab                 = HabisinLime,                       // FAB stays in the nav's green family
    onFab               = HabisinOlive,
    limeCard            = HabisinLightGray,                  // neutral cards
    onLimeCard          = HabisinTextDark,
    peachCard           = HabisinLightGray,
    onPeachCard         = HabisinTextDark,
    selectedContainer   = HabisinTextDark,                  // inverted neutral = hueless selection
    onSelectedContainer = HabisinWhite,
    attentionCard       = HabisinLightGray,                 // neutral alert card (+ coral count accent at call site)
    onAttentionCard     = HabisinTextDark,
    attentionRow        = HabisinWhite,
    fieldBg             = HabisinLightGray,
    fieldHint           = HabisinTextMuted,
    divider             = DividerGray,
)

private val DarkHabisinColors = HabisinColors(
    action              = HabisinCoralDarkMode,   // softened coral, buttons only
    onAction            = HabisinWhite,
    textMuted           = HabisinDarkMuted,
    navBar              = HabisinDarkNav,         // the only green in dark
    onNavBar            = HabisinDarkText.copy(alpha = 0.55f),
    navSelected         = HabisinDarkText,        // bright white selected, no coral
    fab                 = HabisinDarkFab,
    onFab               = HabisinDarkFabIcon,
    limeCard            = HabisinDarkSurface2,     // neutral
    onLimeCard          = HabisinDarkText,
    peachCard           = HabisinDarkSurface2,
    onPeachCard         = HabisinDarkText,
    selectedContainer   = HabisinDarkText,         // inverted near-white = hueless selection
    onSelectedContainer = HabisinDarkBg,
    attentionCard       = HabisinDarkSurface2,     // neutral
    onAttentionCard     = HabisinDarkText,
    attentionRow        = HabisinDarkSurface,
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

//Dark scheme — "Verdant Dark". Opaque surfaces, coral stays the interactive color (softened).
private val HabisinDarkColors = darkColorScheme(
    primary          = HabisinCoralDarkMode,
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
