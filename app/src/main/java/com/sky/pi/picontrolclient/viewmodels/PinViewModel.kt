package com.sky.pi.picontrolclient.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sky.pi.picontrolclient.models.OperationData
import com.sky.pi.picontrolclient.models.Pin
import com.sky.pi.picontrolclient.repo.interfaces.PiAccessRepo
import com.sky.pi.picontrolclient.repo.interfaces.PinRepo
import kotlinx.coroutines.launch

class PinViewModel(private val piAccessRepo: PiAccessRepo, private val pinRepo: PinRepo) : ViewModel() {

    private val _pinListLiveData = MutableLiveData(arrayListOf<Pin>())
    val pinListLive: LiveData<ArrayList<Pin>> = _pinListLiveData

    init {
        viewModelScope.launch {
            piAccessRepo.getInfo("Android Client")
        }
    }

    private fun setSwitch(
        gpioNo: Int,
        operationData: OperationData.SWITCH
    ) = viewModelScope.launch {
        piAccessRepo.setPinState(
            state = operationData.isOn,
            pinNo = gpioNo
        )
    }

    private fun setPwm(
        gpioNo: Int,
        operationData: OperationData.PWM
    ) = viewModelScope.launch {
        piAccessRepo.setPwm(
            pin = gpioNo,
            dutyCycle = operationData.dutyCycle,
            frequency = operationData.frequency
        )
    }

    fun shutdownServer() {
        viewModelScope.launch { piAccessRepo.shutdownServer() }
    }

    fun close(): Unit = piAccessRepo.disconnectServer()

    fun deletePin(pinNo: Int) {
        pinRepo.deletePin(pinNo)
        _pinListLiveData.value = pinRepo.pinList
    }

    fun addPin(pinNo: Int) {
        pinRepo.addPin(pinNo)
        _pinListLiveData.value = pinRepo.pinList
    }

    fun updatePinData(pinNo: Int, operationData: OperationData) {
        val pinData = pinRepo.getPin(pinNo)
        pinRepo.replacePin(pinData.copy(operationData = operationData))
        _pinListLiveData.value = pinRepo.pinList

        when (operationData) {
            is OperationData.INPUT -> TODO()
            is OperationData.SWITCH -> setSwitch(pinData.gpioNo, operationData)
            is OperationData.BLINK -> TODO()
            is OperationData.PWM -> setPwm(pinData.gpioNo, operationData)
            OperationData.NONE -> println("if this is printing then something is wrong")
        }
    }
}