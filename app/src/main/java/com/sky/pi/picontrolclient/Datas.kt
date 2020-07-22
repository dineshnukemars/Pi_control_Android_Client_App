package com.sky.pi.picontrolclient

import com.sky.backend.grpc.pi.BoardInfoResponse

val pinDataArray = listOf(
    PinData(1, -1, arrayOf(GPIOType.POWER_3V)),
    PinData(2, -1, arrayOf(GPIOType.POWER_5V)),
    PinData(3, 2, arrayOf(GPIOType.SDA)),
    PinData(4, -1, arrayOf(GPIOType.POWER_5V)),
    PinData(5, 3, arrayOf(GPIOType.SCL)),
    PinData(6, -1, arrayOf(GPIOType.GROUND)),
    PinData(7, 4, arrayOf(GPIOType.GPCLK0)),
    PinData(8, 14, arrayOf(GPIOType.TXD)),
    PinData(9, -1, arrayOf(GPIOType.GROUND)),
    PinData(10, 15, arrayOf(GPIOType.RXD)),
    PinData(11, 17, emptyArray()),
    PinData(12, 18, arrayOf(GPIOType.PCM_CLK)),
    PinData(13, 27, emptyArray()),
    PinData(14, -1, arrayOf(GPIOType.GROUND)),
    PinData(15, 22, emptyArray()),
    PinData(16, 23, emptyArray()),
    PinData(17, -1, arrayOf(GPIOType.POWER_3V)),
    PinData(18, 24, emptyArray()),
    PinData(19, 10, arrayOf(GPIOType.MOSI)),
    PinData(20, -1, arrayOf(GPIOType.GROUND)),
    PinData(21, 9, arrayOf(GPIOType.MISO)),
    PinData(22, 25, emptyArray()),
    PinData(23, 11, arrayOf(GPIOType.SCLK)),
    PinData(24, 8, arrayOf(GPIOType.CE)),
    PinData(25, -1, arrayOf(GPIOType.GROUND)),
    PinData(26, 7, arrayOf(GPIOType.CE)),
    PinData(27, 0, arrayOf(GPIOType.ID_SD)),
    PinData(28, 1, arrayOf(GPIOType.ID_SC)),
    PinData(29, 5, emptyArray()),
    PinData(30, -1, arrayOf(GPIOType.GROUND)),
    PinData(31, 6, emptyArray()),
    PinData(32, 12, arrayOf(GPIOType.PWM)),
    PinData(33, 13, arrayOf(GPIOType.PWM)),
    PinData(34, -1, arrayOf(GPIOType.GROUND)),
    PinData(35, 19, arrayOf(GPIOType.PCM_FS)),
    PinData(36, 16, emptyArray()),
    PinData(37, 26, emptyArray()),
    PinData(38, 20, arrayOf(GPIOType.PCM_DIN)),
    PinData(39, -1, arrayOf(GPIOType.GROUND)),
    PinData(40, 21, arrayOf(GPIOType.PCM_DOUT))
)

data class PinData(
    val pinNo: Int,
    val gpioNo: Int,
    val gpioType: Array<GPIOType>,
    var activated: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PinData

        if (pinNo != other.pinNo) return false
        if (gpioNo != other.gpioNo) return false
        if (!gpioType.contentEquals(other.gpioType)) return false
        if (activated != other.activated) return false

        return true
    }

    override fun hashCode(): Int {
        var result = pinNo
        result = 31 * result + gpioNo
        result = 31 * result + gpioType.contentHashCode()
        result = 31 * result + activated.hashCode()
        return result
    }
}

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
    PCM_DOUT;

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
