package com.example.habisin.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

//Light scheme
//private val HabisinLightColors = lightColorScheme(
//    primary          = HabisinOlive,
//    onPrimary        = HabisinWhite,
//    secondary        = HabisinTeal,
//    onSecondary      = HabisinWhite,
//    tertiary         = HabisinCoral,
//    onTertiary       = HabisinWhite,
//    background       = HabisinWhite,
//    onBackground     = HabisinTextDark,
//    surface          = HabisinWhite,
//    onSurface        = HabisinTextDark,
//    surfaceVariant   = HabisinLightGray,
//    onSurfaceVariant = HabisinTextMuted
//)
//
////Dark scheme
//private val HabisinDarkColors = darkColorScheme(
//    primary          = HabisinDarkLime,
//    onPrimary        = HabisinWhite,
//    secondary        = HabisinTeal,
//    onSecondary      = HabisinWhite,
//    tertiary         = HabisinDarkPeach,
//    onTertiary       = HabisinBlack,
//    background       = HabisinDarkBg,
//    onBackground     = HabisinDarkText,
//    surface          = HabisinDarkSurface,
//    onSurface        = HabisinDarkText,
//    surfaceVariant   = HabisinDarkSurface,
//    onSurfaceVariant = HabisinDarkMuted
//)

//@Composable
//fun HabisInTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
//    dynamicColor: Boolean = false,
//    content: @Composable () -> Unit
//) {
//    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//        darkTheme -> HabisinDarkColors
//        else      -> HabisinLightColors
//    }
//
//    MaterialTheme(
//        colorScheme = colorScheme,
//        typography  = Typography,
//        content     = content
//    )
//}