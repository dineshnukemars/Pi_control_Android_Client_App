package com.sky.pi.client.ui.compose.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val primaryColor = Color(0xfffdd835)
val primaryVariant = Color(0xffffff6b)
val secondary = Color(0xffc6a700)

private val DarkColorPalette = darkColors(
    primary = Color.Black,
    primaryVariant = Color.Gray,
    secondary = Color.White
)

private val LightColorPalette = lightColors(
    primary = primaryColor,
    primaryVariant = primaryVariant,
    secondary = secondary,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
)

val typography = Typography()

object Styles {

    val topBarTitle = typography.h5

    val contentTitle = typography.body1

    val contentSubtitle1 = typography.subtitle2

    val contentSubtitle2 = typography.caption
}

val shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp)
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
        content = content,
        typography = typography
    )
}