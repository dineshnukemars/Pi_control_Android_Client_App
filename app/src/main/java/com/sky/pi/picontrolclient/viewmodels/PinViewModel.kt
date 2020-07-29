package com.sky.pi.picontrolclient.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sky.pi.picontrolclient.models.Operation
import com.sky.pi.picontrolclient.models.Pin
import com.sky.pi.picontrolclient.repo.interfaces.PiRepo
import com.sky.pi.picontrolclient.repo.interfaces.PinRepo
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PinViewModel(private val piRepo: PiRepo, private val pinRepo: PinRepo) :
    ViewModel() {

    private val _pinListLD = MutableLiveData(listOf<Pin>())
    val pinListLD: LiveData<List<Pin>> = _pinListLD

    init {
        viewModelScope.launch {
            piRepo.boardInfo("Android Client")
        }
    }

    fun shutdownServer(): Job = viewModelScope.launch {
        piRepo.shutdownServer()
    }

    fun disconnectServer(): Unit = piRepo.disconnectServer()

    fun deletePin(pinNo: Int) {
        pinRepo.delete(pinNo)
        _pinListLD.value = pinRepo.pinList()
    }

    fun addPin(pinNo: Int) {
        pinRepo.add(pinNo)
        _pinListLD.value = pinRepo.pinList()
    }

    fun updatePin(pinNo: Int, operation: Operation) {
        viewModelScope.launch {
            val pin = pinRepo.pinForNo(pinNo)

            val isSuccess = when (operation) {
                is Operation.INPUT -> TODO()
                is Operation.SWITCH ->
                    piRepo.pinState(
                        state = operation.isOn,
                        pinNo = pin.gpioNo
                    )
                is Operation.BLINK ->
                    piRepo.blink(
                        pin.gpioNo,
                        operation.wavePeriod,
                        operation.highTime
                    )

                is Operation.PWM ->
                    piRepo.pwm(
                        pin = pin.gpioNo,
                        dutyCycle = operation.dutyCycle,
                        frequency = operation.frequency
                    )
                Operation.NONE -> {
                    println("if this is printing then something is wrong")
                    false
                }
            }
            if (isSuccess) pinRepo.updateOperation(pinNo, operation)
            _pinListLD.value = pinRepo.pinList()
        }
    }
}