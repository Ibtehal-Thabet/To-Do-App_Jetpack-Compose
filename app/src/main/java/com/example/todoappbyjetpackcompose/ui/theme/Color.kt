package com.example.todoappbyjetpackcompose.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val LightPrimary = Color(0xFF5D9CEC)
val LightGreen = Color(0xFFDFECDB)
val DarkBackground = Color(0xFF060E1E)
val IsDoneColor = Color(0xFF61E757)
val CardsBackgroundColor = Color(0xFF141922)

sealed class ThemeColors(
    val background: Color,
    val surface: Color,
    val primary: Color,
    val text: Color
) {
    object Night : ThemeColors(
        background = Color.Black,
        surface = LightPrimary,
        primary = LightPrimary,
        text = Color.White
    )

    object Day : ThemeColors(
        background = LightGreen,
        surface = LightPrimary,
        primary = LightPrimary,
        text = Color.Black
    )
}