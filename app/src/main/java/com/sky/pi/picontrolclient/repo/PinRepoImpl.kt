package com.sky.pi.picontrolclient.repo

import com.sky.pi.picontrolclient.models.OperationData
import com.sky.pi.picontrolclient.models.Pin
import com.sky.pi.picontrolclient.models.PinLayout
import com.sky.pi.picontrolclient.repo.interfaces.PinRepo

class PinRepoImpl(private val pinLayout: PinLayout) : PinRepo {
    private val pinList: ArrayList<Pin> = ArrayList()

    override fun pinList(): List<Pin> = pinList

    override fun updateOperationData(
        pinNo: Int,
        operationData: OperationData
    ): Pin {
        val updatedPin = getPin(pinNo).copy(operationData = operationData)
        replacePin(updatedPin)
        return updatedPin
    }

    override fun getPin(pinNo: Int): Pin {
        return pinList.find { it.pinNo == pinNo } ?: throw Error("WTF")
    }

    override fun deletePin(pinNo: Int) {
        val pinData = pinList.find { it.pinNo == pinNo } ?: throw Error("WTF")
        pinList.remove(pinData)
    }

    override fun addPin(pinNo: Int) {
        val pin = pinLayout.pi4bPinList.find { it.pinNo == pinNo }
            ?: throw IllegalStateException("unknown pin")
        if (!pinList.contains(pin)) pinList.add(pin)
    }

    private fun replacePin(updatedPin: Pin) {
        val oldPinDataIndex = pinList.indexOfFirst { it.pinNo == updatedPin.pinNo }
        pinList.removeAt(oldPinDataIndex)
        pinList.add(oldPinDataIndex, updatedPin)
    }
}