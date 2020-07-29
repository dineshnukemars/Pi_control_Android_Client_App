package com.sky.pi.picontrolclient.repo.interfaces

import androidx.lifecycle.LiveData
import com.sky.pi.picontrolclient.models.BoardInfo

interface PiAccessRepo {

    val isServerConnected: LiveData<Boolean>

    suspend fun connectServer(): Boolean

    suspend fun boardInfo(deviceId: String): BoardInfo

    suspend fun pinState(state: Boolean, pinNo: Int): Boolean

    suspend fun pwm(pin: Int, dutyCycle: Float, frequency: Int): Boolean

    suspend fun blink(pin: Int, wavePeriod: Int, highTime: Float): Boolean

    suspend fun listenDigitalInput(deviceId: String): Boolean

    suspend fun shutdownServer()

    fun disconnectServer()
}