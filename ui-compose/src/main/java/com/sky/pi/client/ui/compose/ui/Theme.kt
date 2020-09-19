package com.sky.pi.client.ui.compose.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val primary = Color(0xfffdd835) // yellow
val lightPrimary = Color(0xffffff6b) // light yellow
val darkSecondary = Color(0xffc6a700) // dark yellow
val darkestSecondary = Color(0xff6b5b01) // dark yellow

private val DarkColorPalette = darkColors(
    primary = Color.Black,
    primaryVariant = Color.Gray,
    secondary = Color.White
)

private val LightColorPalette = lightColors(
    primary = primary,
    primaryVariant = lightPrimary,
    secondary = darkSecondary,
    secondaryVariant = darkestSecondary,
    background = Color.White,
    surface = Color.White,
    error = Color.Red,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White
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

fun getSelectableTextColor(selectedPinState: Boolean) =
    if (selectedPinState) primary
    else Color.Black