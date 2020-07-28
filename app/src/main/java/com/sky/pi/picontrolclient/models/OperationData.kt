package com.sky.pi.picontrolclient.models

sealed class OperationData {
    object NONE : OperationData()
    data class INPUT(val readData: String = "Nothing") : OperationData()
    data class SWITCH(val isOn: Boolean = true) : OperationData()
    data class BLINK(val wavePeriod: Int = 1, val highTime: Float = 0.5f) : OperationData()
    data class PWM(val frequency: Int = 1000, val dutyCycle: Float = 0.5f) : OperationData()
}