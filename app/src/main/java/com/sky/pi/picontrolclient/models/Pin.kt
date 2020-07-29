package com.sky.pi.picontrolclient.models

data class Pin(
    val pinNo: Int,
    val gpioNo: Int,
    val pinType: PinType,
    val operation: Operation = Operation.NONE
)