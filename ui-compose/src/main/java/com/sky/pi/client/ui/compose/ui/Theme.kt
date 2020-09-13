package com.sky.pi.client.ui.compose.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

val primaryColor = Color(0xfffdd835)
val primaryLightColor = Color(0xffffff6b)
val primaryDarkColor = Color(0xffc6a700)
val primaryTextColor = Color.Black

private val DarkColorPalette = darkColors(
    primary = Color.Black,
    primaryVariant = Color.Gray,
    secondary = primaryDarkColor
)

private val LightColorPalette = lightColors(
    primary = primaryColor,
    primaryVariant = primaryLightColor,
    secondary = primaryDarkColor,
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
)

val typography = Typography(
    body1 = TextStyle(

        color = primaryTextColor
    ),
    button = TextStyle(
        color = primaryTextColor

    ),
    caption = TextStyle(
        color = primaryTextColor

    )
)

@Composable
fun landingPage(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit,
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        shapes = shapes,
        content = content
    )
}