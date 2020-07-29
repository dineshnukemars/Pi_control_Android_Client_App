package com.sky.pi.picontrolclient.models

class PinLayout {

    private val pi4bPinList = listOf(
        Pin(1, -1, PinType.POWER_3V),
        Pin(2, -1, PinType.POWER_5V),
        Pin(3, 2, PinType.SDA),
        Pin(4, -1, PinType.POWER_5V),
        Pin(5, 3, PinType.SCL),
        Pin(6, -1, PinType.GROUND),
        Pin(7, 4, PinType.GPCLK0),
        Pin(8, 14, PinType.TXD),
        Pin(9, -1, PinType.GROUND),
        Pin(10, 15, PinType.RXD),
        Pin(11, 17, PinType.NONE),
        Pin(12, 18, PinType.PCM_CLK),
        Pin(13, 27, PinType.NONE),
        Pin(14, -1, PinType.GROUND),
        Pin(15, 22, PinType.NONE),
        Pin(16, 23, PinType.NONE),
        Pin(17, -1, PinType.POWER_3V),
        Pin(18, 24, PinType.NONE),
        Pin(19, 10, PinType.MOSI),
        Pin(20, -1, PinType.GROUND),
        Pin(21, 9, PinType.MISO),
        Pin(22, 25, PinType.NONE),
        Pin(23, 11, PinType.SCLK),
        Pin(24, 8, PinType.CE),
        Pin(25, -1, PinType.GROUND),
        Pin(26, 7, PinType.CE),
        Pin(27, 0, PinType.ID_SD),
        Pin(28, 1, PinType.ID_SC),
        Pin(29, 5, PinType.NONE),
        Pin(30, -1, PinType.GROUND),
        Pin(31, 6, PinType.NONE),
        Pin(32, 12, PinType.PWM),
        Pin(33, 13, PinType.PWM),
        Pin(34, -1, PinType.GROUND),
        Pin(35, 19, PinType.PCM_FS),
        Pin(36, 16, PinType.NONE),
        Pin(37, 26, PinType.NONE),
        Pin(38, 20, PinType.PCM_DIN),
        Pin(39, -1, PinType.GROUND),
        Pin(40, 21, PinType.PCM_DOUT)
    )

    fun pinForPinNo(pinNo: Int): Pin =
        pi4bPinList.find { it.pinNo == pinNo } ?: throw IllegalStateException("unknown pin")
}