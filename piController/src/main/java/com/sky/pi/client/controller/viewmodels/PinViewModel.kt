package com.sky.pi.client.controller.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sky.pi.client.libs.livedata.SingleLiveEvent
import com.sky.pi.client.libs.models.Operation
import com.sky.pi.client.libs.models.Pin
import com.sky.pi.client.libs.models.pi4bPinList
import com.sky.pi.repo.pinrepo.PinRepo
import com.sky.pi.repo.pirepo.RaspiRepo
import kotlinx.coroutines.launch

class PinViewModel(
    private val raspiRepo: RaspiRepo,
    private val pinRepo: PinRepo
) : ViewModel() {

    private val _pinListLD = MutableLiveData(listOf<Pin>())
    val pinListLD: LiveData<List<Pin>> = _pinListLD

    val leftPinListLD: List<Pin> = pi4bPinList.asSequence().filter { it.isLeft }.toList()
    val rightPinListLD: List<Pin> = pi4bPinList.asSequence().filter { !it.isLeft }.toList()

    private val _toastLD = SingleLiveEvent<String>()
    val toastLD: LiveData<String> = _toastLD

    init {
        viewModelScope.launch {
            raspiRepo.boardInfo("Android Client")
        }
    }

    fun pinChecked(yes: Boolean, pinNo: Int) {
        if (yes) addPin(pinNo = pinNo)
        else deletePin(pinNo = pinNo)
    }

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