package com.sky.pi.repo.impl

import com.sky.pi.repo.interfaces.IPinRepo
import com.sky.pi.repo.models.Operation
import com.sky.pi.repo.models.Pin

class PinRepoImpl(private val boardPinList: List<Pin>) : IPinRepo {
    private val pinList: ArrayList<Pin> = ArrayList()

    override fun pinList(): List<Pin> = pinList

    override fun updateOperation(
        pinNo: Int,
        operation: Operation
    ): Pin {
        val updatedPin = pinForNo(pinNo).copy(operation = operation)
        replace(updatedPin)
        return updatedPin
    }

    override fun pinForNo(pinNo: Int): Pin {
        return pinList.find { it.pinNo == pinNo } ?: throw Error("WTF")
    }

    override fun delete(pinNo: Int) {
        val pin = pinList.find { it.pinNo == pinNo } ?: throw Error("WTF")
        pinList.remove(pin)
    }

    override fun add(pinNo: Int) {
        val pin = boardPinList.find { it.pinNo == pinNo } ?: throw Error("WTF")
        if (!pinList.contains(pin)) pinList.add(pin) else throw Error("Already added")
    }

    private fun replace(updatedPin: Pin) {
        val oldPinIndex = pinList.indexOfFirst { it.pinNo == updatedPin.pinNo }
        pinList.removeAt(oldPinIndex)
        pinList.add(oldPinIndex, updatedPin)
    }
}