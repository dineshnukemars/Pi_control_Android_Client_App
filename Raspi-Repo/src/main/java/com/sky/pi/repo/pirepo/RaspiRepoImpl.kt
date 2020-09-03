package com.sky.pi.repo.pirepo


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sky.backend.grpc.pi.PiAccessGrpc
import com.sky.pi.client.libs.models.Operation
import com.sky.pi.repo.pirepo.builders.*
import com.sky.pi.repo.pirepo.entities.BoardInfo
import io.grpc.ManagedChannel
import java.util.concurrent.TimeUnit

class RaspiRepoImpl(
    private val ipAddress: String,
    private val port: Int
) : RaspiRepo {

    private var grpcChannel: ManagedChannel? = null
    private lateinit var _grpcStub: PiAccessGrpc.PiAccessBlockingStub

    private val _isServerConnected = MutableLiveData<Boolean>()
    override val isServerConnected: LiveData<Boolean> = _isServerConnected

    override suspend fun connectServer(): Boolean {
        val managedChannel = createChannel(ipAddress, port)
        grpcChannel = managedChannel
        _grpcStub = PiAccessGrpc.newBlockingStub(grpcChannel)
        _isServerConnected.value = true

        return true
    }

    override suspend fun boardInfo(deviceId: String): BoardInfo {
        val request = generalRequest("Android")
        return BoardInfo(
            _grpcStub.boardInfo(
                request
            )
        )
    }

    override suspend fun pinState(pinNo: Int, operation: Operation.SWITCH): Boolean {
        val response = _grpcStub.switch_(
            switchStateRequest(
                isOn = operation.isOn,
                pinNo = pinNo
            )
        )
        return response.isCommandSuccess
    }

    override suspend fun pwm(pin: Int, operation: Operation.PWM): Boolean {
        val request = pwmRequest(pin, operation)
        val pwm = _grpcStub.pwm(request)
        return pwm.isCommandSuccess
    }

    override suspend fun blink(pin: Int, operation: Operation.BLINK): Boolean {
        val request = blinkRequest(pin, operation)
        val blink = _grpcStub.blink(request)
        return blink.isCommandSuccess
    }

    override suspend fun listenDigitalInput(deviceId: String): Boolean {
        TODO("no backend implemented and flow")
    }

    override suspend fun shutdownServer() {
        val response = _grpcStub.shutdownServer(
            generalRequest(
                "Android"
            )
        )
        println("server shutdown ${response.isCommandSuccess}")
    }

    override fun disconnectServer() {
        grpcChannel?.shutdown()?.awaitTermination(5, TimeUnit.SECONDS)
            ?: throw Error("grpcChannel is null")
        _isServerConnected.value = false
        println("connection to server closed")
    }
}