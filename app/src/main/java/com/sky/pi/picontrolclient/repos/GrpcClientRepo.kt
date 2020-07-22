package com.sky.pi.picontrolclient.repos

import com.sky.backend.grpc.pi.GeneralRequest
import com.sky.backend.grpc.pi.PiAccessGrpcKt
import com.sky.backend.grpc.pi.PwmRequest
import com.sky.backend.grpc.pi.SwitchState
import com.sky.pi.picontrolclient.BoardInfo
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import java.util.concurrent.TimeUnit

class GrpcClientRepo(ipAddress: String, port: Int) : PiAccessRepo {
    private val channel: ManagedChannel by lazy { createChannel(ipAddress, port) }

    private val stub: PiAccessGrpcKt.PiAccessCoroutineStub by lazy {
        PiAccessGrpcKt.PiAccessCoroutineStub(channel)
    }

    override suspend fun getInfo(deviceId: String): BoardInfo {
        val request = generalRequest("Android")
        return BoardInfo(stub.getBoardInfo(request))
    }

    override suspend fun setPinState(state: Boolean, pinNo: Int): Boolean {
        val response = stub.setSwitchState(switchStateRequest(isOn = state, pinNo = pinNo))
        return response.isCommandSuccess
    }

    override suspend fun setPwm(pin: Int, dutyCycle: Float, frequency: Int): Boolean {
        val newBuilder = PwmRequest.newBuilder()
        newBuilder.pin = pin
        newBuilder.dutyCycle = dutyCycle
        newBuilder.frequency = frequency
        val pwm = stub.setPwm(newBuilder.build())
        return pwm.isCommandSuccess
    }

    override suspend fun shutdownServer() {
        val response = stub.shutdown(generalRequest("Android"))
        println("shut ${response.isCommandSuccess}")
    }

    override fun close() {
        println("shutdown channel")
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
        println(" channel shutdown completed")
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
            .build()
    }
}


