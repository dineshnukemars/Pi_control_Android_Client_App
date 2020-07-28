package com.sky.pi.picontrolclient.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sky.backend.grpc.pi.GeneralRequest
import com.sky.backend.grpc.pi.PiAccessGrpcKt
import com.sky.backend.grpc.pi.PwmRequest
import com.sky.backend.grpc.pi.SwitchState
import com.sky.pi.picontrolclient.models.BoardInfo
import com.sky.pi.picontrolclient.repo.interfaces.PiAccessRepo
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import java.util.concurrent.TimeUnit

class PiAccessRepoImpl(private val ipAddress: String, private val port: Int) :
    PiAccessRepo {
    private var grpcChannel: ManagedChannel? = null
    private var grpcStub: PiAccessGrpcKt.PiAccessCoroutineStub? = null

    private val _isServerActive = MutableLiveData(false)
    override val isServerActive: LiveData<Boolean> = _isServerActive

    private val _isServerConnected = MutableLiveData(false)
    override val isServerConnected: LiveData<Boolean> = _isServerConnected

    override suspend fun startServer(): Boolean {
        _isServerActive.value = true
        TODO("Not yet implemented")
    }

    override suspend fun connectServer(): Boolean {
        val managedChannel = createChannel(ipAddress, port)
        grpcChannel = managedChannel
        grpcStub = PiAccessGrpcKt.PiAccessCoroutineStub(managedChannel)
        _isServerConnected.value = true
        return true
    }

    override suspend fun getInfo(deviceId: String): BoardInfo {
        val request = generalRequest("Android")
        return BoardInfo(
            getStub().getBoardInfo(
                request
            )
        )
    }

    override suspend fun setPinState(state: Boolean, pinNo: Int): Boolean {
        val response = getStub().setSwitchState(switchStateRequest(isOn = state, pinNo = pinNo))
        return response.isCommandSuccess
    }

    override suspend fun setPwm(pin: Int, dutyCycle: Float, frequency: Int): Boolean {
        val newBuilder = PwmRequest.newBuilder()
        newBuilder.pin = pin
        newBuilder.dutyCycle = dutyCycle
        newBuilder.frequency = frequency
        val pwm = getStub().setPwm(newBuilder.build())
        return pwm.isCommandSuccess
    }

    override suspend fun shutdownServer() {
        val response = getStub().shutdown(generalRequest("Android"))
        _isServerActive.value = false
        println("server shutdown ${response.isCommandSuccess}")
    }

    override fun disconnectServer() {
        grpcChannel?.shutdown()?.awaitTermination(5, TimeUnit.SECONDS)
            ?: throw Error("grpcChannel is null")
        _isServerConnected.value = false
        println("connection to server closed")
    }

    private fun getStub(): PiAccessGrpcKt.PiAccessCoroutineStub {
        return grpcStub ?: throw Error("Stub is null")
    }

    private fun generalRequest(deviceId: String): GeneralRequest {
        return GeneralRequest
            .newBuilder()
            .setDeviceID(deviceId)
            .build()
    }

    private fun switchStateRequest(isOn: Boolean, pinNo: Int): SwitchState {
        val builder = SwitchState.newBuilder()
        builder.isOn = isOn
        builder.pinNo = pinNo
        return builder.build()
    }

    private fun createChannel(ipAddress: String, port: Int): ManagedChannel {
        return ManagedChannelBuilder
            .forAddress(ipAddress, port)
            .usePlaintext()
            .executor(Dispatchers.Default.asExecutor())
            .build() ?: throw Error("Could not create Channel")
    }
}


