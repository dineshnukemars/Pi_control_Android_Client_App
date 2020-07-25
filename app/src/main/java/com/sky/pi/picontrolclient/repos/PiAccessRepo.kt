package com.sky.pi.picontrolclient.repos

import androidx.lifecycle.LiveData
import com.sky.pi.picontrolclient.BoardInfo

interface PiAccessRepo {

    val isServerActive: LiveData<Boolean>
    val isServerConnected: LiveData<Boolean>

    suspend fun startServer(): Boolean

    suspend fun connectServer(): Boolean

    suspend fun getInfo(deviceId: String): BoardInfo

    suspend fun setPinState(state: Boolean, pinNo: Int): Boolean

    suspend fun setPwm(pin: Int, dutyCycle: Float, frequency: Int): Boolean

    suspend fun shutdownServer()

    fun disconnectServer()
}

