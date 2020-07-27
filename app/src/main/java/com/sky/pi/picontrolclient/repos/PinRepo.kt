package com.sky.pi.picontrolclient.repos

import com.sky.pi.picontrolclient.PinData
import com.sky.pi.picontrolclient.pinDataArray

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