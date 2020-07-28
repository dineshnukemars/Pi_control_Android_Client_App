package com.sky.pi.picontrolclient.models

enum class PinType {
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