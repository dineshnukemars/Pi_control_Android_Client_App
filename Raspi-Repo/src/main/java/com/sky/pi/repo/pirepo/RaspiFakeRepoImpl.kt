package com.sky.pi.repo.pirepo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sky.pi.client.libs.models.Operation
import com.sky.pi.repo.pirepo.entities.BoardInfo

class RaspiFakeRepoImpl : RaspiRepo {
    var commandSuccess = true

    private val _isServerConnected = MutableLiveData<Boolean>()
    override val isServerConnected: LiveData<Boolean> = _isServerConnected

    override suspend fun connectServer(): Boolean {
        _isServerConnected.value = true
        println("fake connectServer")
        return commandSuccess
    }

    override suspend fun boardInfo(deviceId: String) =
        BoardInfo(
            make = "fakeMake",
            model = "fakeModel",
            memory = 8000,
            libraryPath = "/fakePath/fakeFile",
            adcVRef = 0.25f
        )

    override suspend fun pinState(pinNo: Int, operation: Operation.SWITCH): Boolean {
        println("fake setPinState ${operation.isOn} $pinNo")
        return commandSuccess
    }

    override suspend fun pwm(pin: Int, operation: Operation.PWM): Boolean {
        println("fake setPwm $pin ${operation.dutyCycle} ${operation.frequency}")
        return commandSuccess
    }

    override suspend fun blink(pin: Int, operation: Operation.BLINK): Boolean {
        println("fake blink $pin ${operation.wavePeriod} ${operation.highTime}")
        return commandSuccess
    }

    override suspend fun listenDigitalInput(deviceId: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun shutdownServer() {
        println("only allowed in dev build")
    }

    override fun disconnectServer() {
        println("close connection")
        _isServerConnected.value = false
    }
}