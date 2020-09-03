package com.sky.pi.repo.pirepo.builders

import com.sky.backend.grpc.pi.BlinkRequest
import com.sky.backend.grpc.pi.GeneralRequest
import com.sky.backend.grpc.pi.PwmRequest
import com.sky.backend.grpc.pi.SwitchState
import com.sky.pi.client.libs.models.Operation
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor


internal fun pwmRequest(
    pin: Int,
    operation: Operation.PWM
): PwmRequest = with(PwmRequest.newBuilder()) {
    this.pin = pin
    dutyCycle = operation.dutyCycle
    frequency = operation.frequency
    build()
}

internal fun blinkRequest(
    pin: Int,
    operation: Operation.BLINK
): BlinkRequest = with(BlinkRequest.newBuilder()) {
    this.pin = pin
    highTime = operation.highTime
    wavePeriod = operation.wavePeriod
    build()
}

internal fun generalRequest(deviceId: String) = GeneralRequest
    .newBuilder()
    .setDeviceID(deviceId)
    .build()

internal fun switchStateRequest(isOn: Boolean, pinNo: Int): SwitchState =
    with(SwitchState.newBuilder()) {
        this.isOn = isOn
        this.pinNo = pinNo
        build()
    }

internal fun createChannel(ipAddress: String, port: Int): ManagedChannel = ManagedChannelBuilder
    .forAddress(ipAddress, port)
    .usePlaintext()
    .executor(Dispatchers.Default.asExecutor())
    .build() ?: throw Error("Could not create Channel")
