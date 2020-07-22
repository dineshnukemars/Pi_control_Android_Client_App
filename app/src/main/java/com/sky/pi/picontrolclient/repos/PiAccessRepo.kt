package com.sky.pi.picontrolclient.repos

import com.sky.pi.picontrolclient.BoardInfo

interface PiAccessRepo {
    suspend fun getInfo(deviceId: String): BoardInfo

    suspend fun setPinState(state: Boolean, pinNo: Int): Boolean

    suspend fun setPwm(pin: Int, dutyCycle: Float, frequency: Int): Boolean

    suspend fun shutdownServer()

    fun close()
}

