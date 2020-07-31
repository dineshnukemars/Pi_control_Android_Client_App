package com.sky.pi.repo.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sky.backend.grpc.pi.*
import com.sky.pi.repo.interfaces.PiRepo
import com.sky.pi.repo.models.BoardInfo
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import java.util.concurrent.TimeUnit

class PiRepoImpl(private val ipAddress: String, private val port: Int) :
    PiRepo {
    private var grpcChannel: ManagedChannel? = null
    private var grpcStub: PiAccessGrpcKt.PiAccessCoroutineStub? = null

    private val _isServerConnected = MutableLiveData<Boolean>()
    override val isServerConnected: LiveData<Boolean> = _isServerConnected

    override suspend fun connectServer(): Boolean {
        val managedChannel = createChannel(ipAddress, port)
        grpcChannel = managedChannel
        grpcStub = PiAccessGrpcKt.PiAccessCoroutineStub(managedChannel)
        _isServerConnected.value = true
        return true
    }

    override suspend fun boardInfo(deviceId: String): BoardInfo {
        val request = generalRequest("Android")
        return BoardInfo(
            getStub().boardInfo(
                request
            )
        )
    }

    override suspend fun pinState(state: Boolean, pinNo: Int): Boolean {
        val response = getStub().switch(switchStateRequest(isOn = state, pinNo = pinNo))
        return response.isCommandSuccess
    }

    override suspend fun pwm(pin: Int, dutyCycle: Float, frequency: Int): Boolean {
        val newBuilder = PwmRequest.newBuilder()
        newBuilder.pin = pin
        newBuilder.dutyCycle = dutyCycle
        newBuilder.frequency = frequency
        val pwm = getStub().pwm(newBuilder.build())
        return pwm.isCommandSuccess
    }

    override suspend fun blink(pin: Int, wavePeriod: Int, highTime: Float): Boolean {
        val newBuilder = BlinkRequest.newBuilder()
        newBuilder.pin = pin
        newBuilder.highTime = highTime
        newBuilder.wavePeriod = wavePeriod
        val blink = getStub().blink(newBuilder.build())
        return blink.isCommandSuccess
    }

    override suspend fun listenDigitalInput(deviceId: String): Boolean {
        TODO("no backend implemented and flow")
    }

    override suspend fun shutdownServer() {
        val response = getStub().shutdownServer(generalRequest("Android"))
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


