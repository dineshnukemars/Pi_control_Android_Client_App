package com.sky.pi.client.libs.models

data class Pin(
    val pinNo: Int,
    val gpioNo: Int,
    val pinType: PinType,
    val operation: Operation = Operation.NONE
)