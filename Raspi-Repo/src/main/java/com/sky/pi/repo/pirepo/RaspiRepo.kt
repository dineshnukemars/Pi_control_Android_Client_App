package com.sky.pi.repo.pirepo

import androidx.lifecycle.LiveData
import com.sky.pi.client.libs.models.Operation
import com.sky.pi.repo.pirepo.entities.BoardInfo


interface RaspiRepo {

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