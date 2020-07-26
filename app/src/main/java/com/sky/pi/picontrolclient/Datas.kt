package com.sky.pi.picontrolclient

import com.sky.backend.grpc.pi.BoardInfoResponse

val pinDataArray = listOf(
    PinData(1, -1, GPIOType.POWER_3V),
    PinData(2, -1, GPIOType.POWER_5V),
    PinData(3, 2, GPIOType.SDA),
    PinData(4, -1, GPIOType.POWER_5V),
    PinData(5, 3, GPIOType.SCL),
    PinData(6, -1, GPIOType.GROUND),
    PinData(7, 4, GPIOType.GPCLK0),
    PinData(8, 14, GPIOType.TXD),
    PinData(9, -1, GPIOType.GROUND),
    PinData(10, 15, GPIOType.RXD),
    PinData(11, 17, GPIOType.NONE),
    PinData(12, 18, GPIOType.PCM_CLK),
    PinData(13, 27, GPIOType.NONE),
    PinData(14, -1, GPIOType.GROUND),
    PinData(15, 22, GPIOType.NONE),
    PinData(16, 23, GPIOType.NONE),
    PinData(17, -1, GPIOType.POWER_3V),
    PinData(18, 24, GPIOType.NONE),
    PinData(19, 10, GPIOType.MOSI),
    PinData(20, -1, GPIOType.GROUND),
    PinData(21, 9, GPIOType.MISO),
    PinData(22, 25, GPIOType.NONE),
    PinData(23, 11, GPIOType.SCLK),
    PinData(24, 8, GPIOType.CE),
    PinData(25, -1, GPIOType.GROUND),
    PinData(26, 7, GPIOType.CE),
    PinData(27, 0, GPIOType.ID_SD),
    PinData(28, 1, GPIOType.ID_SC),
    PinData(29, 5, GPIOType.NONE),
    PinData(30, -1, GPIOType.GROUND),
    PinData(31, 6, GPIOType.NONE),
    PinData(32, 12, GPIOType.PWM),
    PinData(33, 13, GPIOType.PWM),
    PinData(34, -1, GPIOType.GROUND),
    PinData(35, 19, GPIOType.PCM_FS),
    PinData(36, 16, GPIOType.NONE),
    PinData(37, 26, GPIOType.NONE),
    PinData(38, 20, GPIOType.PCM_DIN),
    PinData(39, -1, GPIOType.GROUND),
    PinData(40, 21, GPIOType.PCM_DOUT)
)

data class PinData(
    val pinNo: Int,
    val gpioNo: Int,
    val gpioType: GPIOType,
    val operationType: OperationType = OperationType.NONE
)

enum class GPIOType {
    GROUND,
    POWER_5V,
    POWER_3V,
    SDA,
    SCL,
    GPCLK0,
    MOSI,
    MISO,
    SCLK,
    ID_SD,
    PWM,
    PCM_FS,
    TXD,
    RXD,
    PCM_CLK,
    CE,
    ID_SC,
    PCM_DIN,
    PCM_DOUT,
    NONE;

    override fun toString(): String {
        return this.name
    }
}

data class BoardInfo(
    val make: String,
    val model: String,
    val memory: Int,
    val libraryPath: String,
    val adcVRef: Float
) {

    constructor(boardInfoResponse: BoardInfoResponse) : this(
        make = boardInfoResponse.make,
        model = boardInfoResponse.model,
        memory = boardInfoResponse.memory,
        libraryPath = boardInfoResponse.libraryPath,
        adcVRef = boardInfoResponse.adcVRef
    )
}

sealed class OperationType {
    object NONE : OperationType()
    class INPUT(val readData: String) : OperationType()
    class SWITCH(val isOn: Boolean) : OperationType()
    class BLINK(val wavePeriod: Int, highTime: Float) : OperationType()
    class PWM(val frequency: Int, val dutyCycle: Float) : OperationType()
}