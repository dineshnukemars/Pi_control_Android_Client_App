package com.sky.pi.repo.models

sealed class Operation {
    object NONE : Operation()
    data class INPUT(val readData: String = "Nothing") : Operation()
    data class SWITCH(val isOn: Boolean = true) : Operation()
    data class BLINK(val wavePeriod: Int = 1, val highTime: Float = 0.5f) : Operation()
    data class PWM(val frequency: Int = 1000, val dutyCycle: Float = 0.5f) : Operation()
}