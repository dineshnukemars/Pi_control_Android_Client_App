package com.sky.pi.picontrolclient.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sky.pi.picontrolclient.models.BoardInfo
import com.sky.pi.picontrolclient.repo.interfaces.PiAccessRepo

class FakePiAccessRepoImpl : PiAccessRepo {
    var commandSuccess = true
    private val _isServerActive = MutableLiveData(false)
    override val isServerActive: LiveData<Boolean> = _isServerActive

    private val _isServerConnected = MutableLiveData(false)
    override val isServerConnected: LiveData<Boolean> = _isServerConnected

    override suspend fun startServer(): Boolean {
        _isServerActive.value = true
        println("fake startServer")
        return commandSuccess
    }

    override suspend fun connectServer(): Boolean {
        _isServerConnected.value = true
        println("fake connectServer")
        return commandSuccess
    }

    override suspend fun getInfo(deviceId: String): BoardInfo {
        println("fake getInfo")
        return BoardInfo(
            make = "fakeMake",
            model = "fakeModel",
            memory = 8000,
            libraryPath = "/fakePath/fakeFile",
            adcVRef = 0.25f
        )
    }

    override suspend fun setPinState(state: Boolean, pinNo: Int): Boolean {
        println("fake setPinState $state $pinNo")
        return commandSuccess
    }

    override suspend fun setPwm(pin: Int, dutyCycle: Float, frequency: Int): Boolean {
        println("fake setPwm $pin $dutyCycle $frequency")
        return commandSuccess
    }

    override suspend fun shutdownServer() {
        println("shutting down Server")
        _isServerActive.value = false
    }

    override fun disconnectServer() {
        println("close connection")
        _isServerConnected.value = false
    }
}