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

    private val selectedPinList: ArrayList<PinData> = ArrayList()
    private var currentPinData: PinData? = null

    private val _pinListLiveData = MutableLiveData(selectedPinList)
    val pinListLiveData: LiveData<ArrayList<PinData>> = _pinListLiveData

    private val _errorMessageLD = SingleLiveEvent<AppMessages>()
    val errorMessageLD: LiveData<AppMessages> = _errorMessageLD

    private val _navigationLD = SingleLiveEvent<Navigate>()
    val navigationLD: LiveData<Navigate> = _navigationLD

    init {
        viewModelScope.launch {
            repo.getInfo("Android Client")
        }
    }

    fun pinClicked(pinNo: Int, checked: Boolean) {
        val foundPinData: PinData =
            pinDataArray.find { it.pinNo == pinNo } ?: throw IllegalStateException("unknown pin")

        val contains = selectedPinList.contains(foundPinData)
        if (contains && !checked)
            selectedPinList.remove(foundPinData)
        else if (!contains)
            selectedPinList.add(foundPinData)

        _pinListLiveData.value = selectedPinList
    }

    fun setItemAction(itemAction: ItemAction, pinData: PinData) {
        val selectedPinData = getSelectedPinFromList(pinData.pinNo)

        when (itemAction) {
            ItemAction.RemovePin -> {

                selectedPinList.remove(selectedPinData)
                _pinListLiveData.value = selectedPinList
                currentPinData = null
            }
            ItemAction.ConfigurePin -> {
                _navigationLD.value = Navigate.CONFIG_DIALOG
                currentPinData = selectedPinData
            }
            ItemAction.UpdateData -> {

                currentPinData = selectedPinData
                when (pinData.operationType) {
                    OperationType.NONE -> TODO()
                    is OperationType.INPUT -> TODO()
                    is OperationType.SWITCH -> TODO()
                    is OperationType.BLINK -> TODO()
                    is OperationType.PWM -> TODO()
                }
            }
        }
    }


    fun setSelectedPinOnList(pinNo: Int) {
        currentPinData = getSelectedPinFromList(pinNo)
    }

    fun setSwitchState(checked: Boolean) {
        viewModelScope.launch {
            val pinState = repo.setPinState(checked, assertAndGetCurrentPinData().gpioNo)
            println(pinState)
        }
    }

    fun setPwm(progress: Int, frequency: Int) {
        viewModelScope.launch {
            repo.setPwm(assertAndGetCurrentPinData().gpioNo, progress.toFloat() / 100, frequency)
        }
    }

    fun shutdownServer() {
        viewModelScope.launch {
            repo.shutdownServer()
        }
    }

    fun close() {
        repo.disconnectServer()
    }

    private fun assertAndGetCurrentPinData() = currentPinData ?: throw error("no pin selected")

    private fun getSelectedPinFromList(pinNo: Int): PinData {
        return selectedPinList.find {
            it.pinNo == pinNo
        } ?: throw IllegalStateException("unknown pin")
    }
}

enum class AppMessages(val message: String) {
    CONNECTION_ERROR("Could not connect to server"),
    COMMAND_SUCCESS("Command success"),
    COMMAND_FAILED("command failed")
}

enum class Navigate {
    CONFIG_DIALOG,
    PIN_LIST_FRAGMENT
}
