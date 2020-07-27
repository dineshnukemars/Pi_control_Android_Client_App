package com.sky.pi.picontrolclient.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sky.pi.picontrolclient.OperationData
import com.sky.pi.picontrolclient.PinData
import com.sky.pi.picontrolclient.pinDataArray
import com.sky.pi.picontrolclient.repos.PiAccessRepo
import kotlinx.coroutines.launch

class PinViewModel(val repo: PiAccessRepo, val pinRepo: PinRepo) : ViewModel() {

    private val _pinListLiveData = MutableLiveData(arrayListOf<PinData>())
    val pinListLiveData: LiveData<ArrayList<PinData>> = _pinListLiveData

    init {
        viewModelScope.launch {
            repo.getInfo("Android Client")
        }
    }

    private fun setSwitch(
        gpioNo: Int,
        operationData: OperationData.SWITCH
    ) {
        viewModelScope.launch {
            repo.setPinState(
                operationData.isOn,
                gpioNo
            )
        }
    }

    private fun setPwm(
        gpioNo: Int,
        operationData: OperationData.PWM
    ) {
        viewModelScope.launch {
            repo.setPwm(
                gpioNo,
                operationData.dutyCycle / 100,
                operationData.frequency
            )
        }
    }

    fun shutdownServer() {
        viewModelScope.launch { repo.shutdownServer() }
    }

    fun close() {
        repo.disconnectServer()
    }

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

class PinRepo {
    val pinList: ArrayList<PinData> = ArrayList()

    fun replacePin(updatedPinData: PinData) {
        val oldPinDataIndex = pinList.indexOfFirst { it.pinNo == updatedPinData.pinNo }
        pinList.removeAt(oldPinDataIndex)
        pinList.add(oldPinDataIndex, updatedPinData)
    }

    fun getPin(pinNo: Int): PinData {
        return pinList.find { it.pinNo == pinNo } ?: throw Error("WTF")
    }

    fun deletePin(pinNo: Int) {
        val pinData = pinList.find { it.pinNo == pinNo } ?: throw Error("WTF")
        pinList.remove(pinData)
    }

    fun addPin(pinNo: Int) {
        val foundPinData: PinData =
            pinDataArray.find { it.pinNo == pinNo } ?: throw IllegalStateException("unknown pin")

        if (!pinList.contains(foundPinData)) pinList.add(foundPinData)
    }
}