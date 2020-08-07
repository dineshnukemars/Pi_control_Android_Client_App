package com.sky.pi.repo.interfaces

import androidx.lifecycle.LiveData
import com.sky.pi.repo.models.BoardInfo
import com.sky.pi.repo.models.Operation

interface IPiRepo {

    val isServerConnected: LiveData<Boolean>

    suspend fun connectServer(): Boolean

    suspend fun boardInfo(deviceId: String): BoardInfo

    suspend fun pinState(pinNo: Int, operation: Operation.SWITCH): Boolean

    suspend fun pwm(pin: Int, operation: Operation.PWM): Boolean

    suspend fun blink(pin: Int, operation: Operation.BLINK): Boolean

    suspend fun listenDigitalInput(deviceId: String): Boolean

    suspend fun shutdownServer()

    fun disconnectServer()
}