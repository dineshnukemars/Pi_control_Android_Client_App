package com.sky.pi.picontrolclient.viewmodels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _radioGroupLD = MutableLiveData(View.GONE)
    val radioGroupLD: LiveData<Int> = _radioGroupLD

    private val _blinkContainerLD = MutableLiveData(View.GONE)
    val blinkContainerLD: LiveData<Int> = _blinkContainerLD

    private val _switchContainerLD = MutableLiveData(View.GONE)
    val switchContainerLD: LiveData<Int> = _switchContainerLD

    private val _pwmContainerLD = MutableLiveData(View.GONE)
    val pwmContainerLD: LiveData<Int> = _pwmContainerLD

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
        when (itemAction) {
            ItemAction.RemovePin -> {
                val foundPinData = selectedPinList.find {
                    it.pinNo == pinData.pinNo
                } ?: throw IllegalStateException("unknown pin")
                selectedPinList.remove(foundPinData)
                _pinListLiveData.value = selectedPinList
            }
            ItemAction.ConfigurePin -> _navigationLD.value = Navigate.CONFIG_DIALOG
            ItemAction.UpdateData -> TODO()
        }
    }

    fun setSelectedPinOnList(pinNo: Int) {
        val pinData = selectedPinList.find { it.pinNo == pinNo }
            ?: throw IllegalStateException("unknown pin")
        currentPinData = pinData
        _radioGroupLD.value = if (pinData.gpioNo > 0) View.VISIBLE else View.GONE
    }

    fun setRadioSelected(radioTag: String) {
        println(radioTag)
        when (radioTag) {
            RadioSelected.INPUT.tag -> setVisibility()
            RadioSelected.BLINK.tag -> setVisibility(isBlink = true)
            RadioSelected.PWM.tag -> setVisibility(isPwm = true)
            RadioSelected.SWITCH.tag -> setVisibility(isSwitch = true)
        }
    }

    private fun setVisibility(
        isBlink: Boolean = false,
        isSwitch: Boolean = false,
        isPwm: Boolean = false
    ) {
        _blinkContainerLD.value = getVisibilityNo(isBlink)
        _switchContainerLD.value = getVisibilityNo(isSwitch)
        _pwmContainerLD.value = getVisibilityNo(isPwm)
    }

    private fun getVisibilityNo(isVisible: Boolean) =
        if (isVisible) View.VISIBLE else View.GONE

    fun setSwitchState(checked: Boolean) {
        viewModelScope.launch {
            val pinData = currentPinData ?: throw error("no pin selected")
            val pinState = repo.setPinState(checked, pinData.gpioNo)
            println(pinState)
        }
    }

    fun shutdownServer() {
        viewModelScope.launch {
            repo.shutdownServer()
        }
    }

    fun setPwm(progress: Int, frequency: Int) {
        viewModelScope.launch {
            val pinData = currentPinData ?: throw error("no pin selected")
            repo.setPwm(pinData.gpioNo, progress.toFloat() / 100, frequency)
        }
    }

    fun close() {
        repo.disconnectServer()
    }
}

enum class RadioSelected(val tag: String) {
    BLINK("radio_blinkV"),
    SWITCH("radio_switchV"),
    PWM("radio_pwmV"),
    INPUT("radio_inputV");
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
