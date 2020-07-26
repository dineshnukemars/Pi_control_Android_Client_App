package com.sky.pi.picontrolclient.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sky.pi.picontrolclient.OperationType
import com.sky.pi.picontrolclient.PinData
import com.sky.pi.picontrolclient.adapters.ItemAction
import com.sky.pi.picontrolclient.pinDataArray
import com.sky.pi.picontrolclient.repos.PiAccessRepo
import com.sky.pi.picontrolclient.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class PinViewModel(val repo: PiAccessRepo) : ViewModel() {

    private val pinList: ArrayList<PinData> = ArrayList()
    private var selectedPinData: PinData? = null

    private val _pinListLiveData = MutableLiveData(pinList)
    val pinListLiveData: LiveData<ArrayList<PinData>> = _pinListLiveData

    private val _showConfigDialog = SingleLiveEvent<Trigger>()
    val showConfigDialog: LiveData<Trigger> = _showConfigDialog

    init {
        viewModelScope.launch {
            repo.getInfo("Android Client")
        }
    }

    private fun setSwitch(
        pinData: PinData,
        operationType: OperationType.SWITCH
    ) {
        viewModelScope.launch {
            repo.setPinState(
                operationType.isOn,
                pinData.gpioNo
            )
        }
    }

    private fun setPwm(
        pinData: PinData,
        operationType: OperationType.PWM
    ) {
        viewModelScope.launch {
            repo.setPwm(
                pinData.gpioNo,
                operationType.dutyCycle / 100,
                operationType.frequency
            )
        }
    }

    fun shutdownServer() {
        viewModelScope.launch { repo.shutdownServer() }
    }

    fun close() {
        repo.disconnectServer()
    }

    fun setRadioSelected(radioTag: String) {
        val pinData = selectedPinData
            ?: throw Error("selected pin is null, really should consider immutability")

        val updatedPinData = when (radioTag) {
            RadioSelected.INPUT.tag -> pinData.copy(operationType = OperationType.INPUT("nothing implemented yet"))
            RadioSelected.BLINK.tag -> pinData.copy(
                operationType = OperationType.BLINK(
                    1,
                    0.5f
                )
            )
            RadioSelected.PWM.tag -> pinData.copy(
                operationType = OperationType.PWM(
                    1000,
                    0.5f
                )
            )
            RadioSelected.SWITCH.tag -> pinData.copy(
                operationType = OperationType.SWITCH(
                    true
                )
            )
            else -> throw Error("if this error appears then something is really bad")
        }
        val oldPinDataIndex = pinList.indexOf(pinData)
        pinList.removeAt(oldPinDataIndex)
        pinList.add(oldPinDataIndex, updatedPinData)
        updatePinData(updatedPinData)
        updatePinListLD()
    }

    private fun updatePinData(pinData: PinData) {
        when (pinData.operationType) {
            is OperationType.INPUT -> TODO()
            is OperationType.SWITCH -> setSwitch(pinData, pinData.operationType)
            is OperationType.BLINK -> TODO()
            is OperationType.PWM -> setPwm(pinData, pinData.operationType)
            OperationType.NONE -> println("if this is printing then something is wrong")
        }
    }

    fun pinClicked(pinNo: Int, checked: Boolean) {
        val foundPinData: PinData =
            pinDataArray.find { it.pinNo == pinNo } ?: throw IllegalStateException("unknown pin")

        val contains = pinList.contains(foundPinData)
        if (contains && !checked)
            pinList.remove(foundPinData)
        else if (!contains)
            pinList.add(foundPinData)

        updatePinListLD()
    }

    private fun updatePinListLD() {
        _pinListLiveData.value = pinList
    }

    fun setItemAction(itemAction: ItemAction, pinData: PinData) {
        selectedPinData = pinData

        when (itemAction) {
            ItemAction.RemovePin -> {
                pinList.remove(pinData)
                _pinListLiveData.value = pinList
                selectedPinData = null
            }
            ItemAction.ConfigurePin -> {
                _showConfigDialog.trigger()
            }
            ItemAction.UpdateData -> {
                updatePinData(pinData = pinData)
            }
        }
    }
}

enum class RadioSelected(val tag: String) {
    BLINK("radio_blinkV"),
    SWITCH("radio_switchV"),
    PWM("radio_pwmV"),
    INPUT("radio_inputV");
}

object Trigger

fun MutableLiveData<Trigger>.trigger() {
    value = Trigger
}