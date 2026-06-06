package com.example.habisin.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

//Brand auth (legacy — kept for source compat; BrandBlue is being retired in favour of the coral action color)
val BrandBlue   = Color(0xFFFF8666) // re-pointed to coral so any straggler reference stays on-brand
val FieldBg     = Color(0xFFEDEDED)
val FieldHint   = Color(0xFF9A9A9A)
val LabelColor  = Color(0xFF2C2C2C)
val DividerGray = Color(0xFFE0E0E0)
val LogoBg      = Color(0xFFF2F4F7)

//Habisin — light brand palette
val HabisinWhite     = Color(0xFFFFFFFF)
val HabisinBlack     = Color(0xFF111111)
val HabisinTextDark  = Color(0xFF292D2F)
val HabisinTextMuted = Color(0xFF666666)
val HabisinLightGray = Color(0xFFEFEFED)
val HabisinCream     = Color(0xFFF5ECE3)
val HabisinPeach     = Color(0xFFFFC79B)
val HabisinCoral     = Color(0xFFFF8666)   // ← THE single action color (buttons, links, CTAs)
val HabisinCoralDark = Color(0xFFE96B4D)   // pressed/contrast variant of the action color
val HabisinLime      = Color(0xFFDDEB8F)
val HabisinOlive     = Color(0xFF4C610D)
val HabisinTeal      = Color(0xFF0F7180)
val HabisinOrange    = Color(0xFFFF9E0D)

//Habisin — "Verdant Dark" palette (clean, opaque surfaces — no transparency)
val HabisinDarkBg        = Color(0xFF14171A)  // app background (near-black, slightly cool)
val HabisinDarkSurface   = Color(0xFF1E2327)  // cards / sheets
val HabisinDarkSurface2  = Color(0xFF272D31)  // raised surface / fields / chips
val HabisinDarkText      = Color(0xFFECEEF0)
val HabisinDarkMuted     = Color(0xFF9BA1A6)
val HabisinDarkDivider   = Color(0xFF333A3F)

// Dark accent surfaces (opaque tints, not alpha overlays)
val HabisinDarkNav       = Color(0xFF1B2410)  // bottom nav (deep olive)
val HabisinDarkLimeCard  = Color(0xFF2E3A18)  // lime accent cards → deep olive in dark
val HabisinDarkLimeText  = Color(0xFFD7E59A)  // text/icon on the deep-olive cards
val HabisinDarkPeachCard = Color(0xFF3A2A22)  // peach/attention cards → warm dark in dark
val HabisinDarkPeachText = Color(0xFFFFC2A6)  // text/icon on the warm-dark cards
val HabisinDarkFab       = Color(0xFF3A4A12)  // FAB surface in dark
