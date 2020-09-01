package com.sky.pi.client.ui.compose

import com.sky.pi.client.libs.models.Pin


fun pinDescription(pin: Pin): String {
    val builder = StringBuilder()
    if (pin.gpioNo != -1) builder.append("(Gpio ${pin.gpioNo})")
    builder.append("${pin.pinType}")
    return builder.toString()
}