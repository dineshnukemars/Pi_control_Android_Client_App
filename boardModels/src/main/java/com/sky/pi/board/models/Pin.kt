package com.sky.pi.board.models

data class Pin(
    val pinNo: Int,
    val gpioNo: Int,
    val pinType: PinType,
    val operation: Operation = Operation.NONE
)