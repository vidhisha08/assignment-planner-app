package com.example.studybuddy.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color



val Lavender50   = Color(0xFFF0EAFF)
val Lavender100  = Color(0xFFE2D9F3)
val Lavender300  = Color(0xFFC9B8F0)
val Lavender600  = Color(0xFF7C6AAD)
val Lavender800  = Color(0xFF4A3872)

val Mint50       = Color(0xFFE4F7F0)
val Mint100      = Color(0xFFC5E8D8)
val Mint400      = Color(0xFF5AAB88)
val Mint700      = Color(0xFF1A6648)

val Blush50      = Color(0xFFFBEAF0)
val Blush100     = Color(0xFFF4C0D1)
val Blush400     = Color(0xFFD4537E)
val Blush700     = Color(0xFF72243E)

val Butter50     = Color(0xFFFEF9E4)
val Butter300    = Color(0xFFF0DFA0)
val Butter700    = Color(0xFF8A6D0A)

val TextDark     = Color(0xFF3D3D3A)
val TextMuted    = Color(0xFF6B6B68)
val White        = Color(0xFFFFFFFF)


private val PastelColorScheme = lightColorScheme(
    primary          = Lavender600,
    onPrimary        = White,
    primaryContainer = Lavender50,
    onPrimaryContainer = Lavender800,

    secondary        = Mint400,
    onSecondary      = White,
    secondaryContainer = Mint50,
    onSecondaryContainer = Mint700,

    tertiary         = Blush400,
    onTertiary       = White,
    tertiaryContainer = Blush50,
    onTertiaryContainer = Blush700,

    background       = Color(0xFFFAF8FF),
    onBackground     = TextDark,
    surface          = White,
    onSurface        = TextDark,
    surfaceVariant   = Lavender50,
    onSurfaceVariant = TextMuted,
    error            = Color(0xFFE24B4A),
    onError          = White
)

@Composable
fun StudyBuddyTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = PastelColorScheme,
        content = content
    )
}
