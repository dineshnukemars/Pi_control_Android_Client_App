package com.sky.pi.picontrolclient.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sky.pi.picontrolclient.livedata.SingleLiveEvent
import com.sky.pi.repo.interfaces.IPiRepo
import com.sky.pi.repo.interfaces.IPinRepo
import com.sky.pi.repo.models.Operation
import com.sky.pi.repo.models.Pin
import kotlinx.coroutines.launch

class PinViewModel(
    private val piRepo: IPiRepo,
    private val pinRepo: IPinRepo
) : ViewModel() {

    private val _pinListLD = MutableLiveData(listOf<Pin>())
    val pinListLD: LiveData<List<Pin>> = _pinListLD

    private val _toastLD =
        SingleLiveEvent<String>()
    val toastLD: LiveData<String> = _toastLD

    init {
        viewModelScope.launch {
            piRepo.boardInfo("Android Client")
        }
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
            val isSuccess = updatePiRepo(operation, pinRepo.pinForNo(pinNo))
            if (isSuccess) {
                pinRepo.updateOperation(pinNo, operation)
                _pinListLD.value = pinRepo.pinList()
            } else {
                _toastLD.value = "Something went Wrong"
            }
        }
    }

    fun disconnectServer(): Unit = piRepo.disconnectServer()

    fun shutdownServer() {
        viewModelScope.launch {
            piRepo.shutdownServer()
        }
    }

    private suspend fun updatePiRepo(
        operation: Operation,
        pin: Pin
    ): Boolean = when (operation) {
        is Operation.INPUT -> TODO()
        is Operation.SWITCH -> piRepo.pinState(pin.gpioNo, operation)
        is Operation.BLINK -> piRepo.blink(pin.gpioNo, operation)
        is Operation.PWM -> piRepo.pwm(pin.gpioNo, operation)
        Operation.NONE -> false
    }
}