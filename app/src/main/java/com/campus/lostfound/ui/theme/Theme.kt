package com.campus.lostfound.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Primary = Color(0xFF2D3FCB)
val LostRed = Color(0xFFE5384F)
val FoundGreen = Color(0xFF16A37F)
val RecoveredGray = Color(0xFF6B7185)
val TextDark = Color(0xFF1B1F3B)
val TextGray = Color(0xFF6B7185)
val TextHint = Color(0xFF9AA0B4)
val Border = Color(0xFFD9DCE6)
val ScreenBg = Color(0xFFF8F9FD)
val TilePink = Color(0xFFFDE8EC)
val TileGreen = Color(0xFFE3F6EF)
val TilePurple = Color(0xFFECE9FB)
val TileOrange = Color(0xFFFDEEDF)
val Orange = Color(0xFFF58A1F)
val Purple = Color(0xFF7B4DE0)

@Composable
fun LostFoundTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Primary, onPrimary = Color.White,
            background = ScreenBg, surface = Color.White,
            onSurface = TextDark, outline = Border
        ),
        content = content
    )
}
