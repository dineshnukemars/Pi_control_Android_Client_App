package com.sky.pi.client.ui.compose

import com.sky.pi.client.libs.models.Pin


fun Pin.description(): String {
    val builder = StringBuilder()
    if (gpioNo != -1) builder.append("(Gpio $gpioNo)")
    builder.append("$pinType")
    return builder.toString()
}

fun List<Pin>.isContainsPin(pin: Pin) = find { it.pinNo == pin.pinNo } != null

fun Int.to2DigitString() = String.format("%02d", this)

fun Pin.isEnabled() = gpioNo != -1