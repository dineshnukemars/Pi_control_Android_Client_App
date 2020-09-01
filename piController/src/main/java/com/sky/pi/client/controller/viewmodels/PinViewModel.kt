package com.sky.pi.client.controller.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sky.pi.client.controller.pinrepo.PinRepo
import com.sky.pi.client.controller.pirepo.RaspiRepo
import com.sky.pi.client.libs.livedata.SingleLiveEvent
import com.sky.pi.client.libs.models.Operation
import com.sky.pi.client.libs.models.Pin
import com.sky.pi.client.libs.models.pi4bPinList
import kotlinx.coroutines.launch

class PinViewModel(
    private val raspiRepo: RaspiRepo,
    private val pinRepo: PinRepo
) : ViewModel() {

    private val _pinListLD = MutableLiveData(listOf<Pin>())
    val pinListLD: LiveData<List<Pin>> = _pinListLD

    private val _toastLD = SingleLiveEvent<String>()
    val toastLD: LiveData<String> = _toastLD

    init {
        viewModelScope.launch {
            raspiRepo.boardInfo("Android Client")
        }
    }

    fun getBoardPins(): List<Pin> = pi4bPinList

    fun addPin(pinNo: Int) {
        pinRepo.add(pinNo)
        _pinListLD.value = pinRepo.pinList()
    }

    fun deletePin(pinNo: Int) {
        pinRepo.delete(pinNo)
        _pinListLD.value = pinRepo.pinList()
    }

    fun updatePin(pinNo: Int, operation: Operation) {
        viewModelScope.launch {
            val isSuccess = updatePiRepo(operation, pinRepo.findPin(pinNo))
            if (isSuccess) {
                pinRepo.updateOperation(pinNo, operation)
                _pinListLD.value = pinRepo.pinList()
            } else {
                _toastLD.value = "Something went Wrong"
            }
        }
    }

    fun disconnectServer(): Unit = raspiRepo.disconnectServer()

    fun shutdownServer() {
        viewModelScope.launch {
            raspiRepo.shutdownServer()
        }
    }

    private suspend fun updatePiRepo(
        operation: Operation,
        pin: Pin
    ): Boolean = when (operation) {
        is Operation.INPUT -> TODO()
        is Operation.SWITCH -> raspiRepo.pinState(pin.gpioNo, operation)
        is Operation.BLINK -> raspiRepo.blink(pin.gpioNo, operation)
        is Operation.PWM -> raspiRepo.pwm(pin.gpioNo, operation)
        Operation.NONE -> false
    }
}