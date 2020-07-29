package com.sky.pi.picontrolclient.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sky.pi.picontrolclient.models.OperationData
import com.sky.pi.picontrolclient.models.Pin
import com.sky.pi.picontrolclient.repo.interfaces.PiAccessRepo
import com.sky.pi.picontrolclient.repo.interfaces.PinRepo
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PinViewModel(private val piRepo: PiAccessRepo, private val pinRepo: PinRepo) :
    ViewModel() {

    private val _pinListLiveData = MutableLiveData(listOf<Pin>())
    val pinListLive: LiveData<List<Pin>> = _pinListLiveData

    init {
        viewModelScope.launch {
            piRepo.boardInfo("Android Client")
        }
    }

    private fun switch(
        gpioNo: Int,
        operationData: OperationData.SWITCH
    ): Job = viewModelScope.launch {
        piRepo.pinState(
            state = operationData.isOn,
            pinNo = gpioNo
        )
    }

    private fun pwm(
        gpioNo: Int,
        operationData: OperationData.PWM
    ): Job = viewModelScope.launch {
        piRepo.pwm(
            pin = gpioNo,
            dutyCycle = operationData.dutyCycle,
            frequency = operationData.frequency
        )
    }

    private fun blink(
        pinData: Pin,
        operationData: OperationData.BLINK
    ): Job = viewModelScope.launch {
        piRepo.blink(
            pinData.gpioNo,
            operationData.wavePeriod,
            operationData.highTime
        )
    }

    fun shutdownServer(): Job = viewModelScope.launch {
        piRepo.shutdownServer()
    }

    fun close(): Unit = piRepo.disconnectServer()

    fun deletePin(pinNo: Int) {
        pinRepo.deletePin(pinNo)
        _pinListLiveData.value = pinRepo.pinList()
    }

    fun addPin(pinNo: Int) {
        pinRepo.addPin(pinNo)
        _pinListLiveData.value = pinRepo.pinList()
    }

    fun updatePinData(pinNo: Int, operationData: OperationData) {
        val pinData = pinRepo.updateOperationData(pinNo, operationData)
        _pinListLiveData.value = pinRepo.pinList()

        when (operationData) {
            is OperationData.INPUT -> TODO()
            is OperationData.SWITCH -> switch(pinData.gpioNo, operationData)
            is OperationData.BLINK -> blink(pinData, operationData)
            is OperationData.PWM -> pwm(pinData.gpioNo, operationData)
            OperationData.NONE -> println("if this is printing then something is wrong")
        }
    }
}