package com.sky.pi.repo.impl

import com.sky.pi.repo.interfaces.IPinRepo
import com.sky.pi.repo.models.Operation
import com.sky.pi.repo.models.Pin

class PinRepoImpl(private val boardPinList: List<Pin>) : IPinRepo {
    private var pinList: List<Pin> = ArrayList()

    override fun pinList(): List<Pin> = pinList

    override fun updateOperation(
        pinNo: Int,
        operation: Operation
    ) {
        pinList = updatePinOnList(pinList, pinNo, operation)
    }

    override fun findPin(pinNo: Int): Pin = findPinElseThrowError(pinList, pinNo)

    override fun delete(pinNo: Int) {
        pinList = deletePinFromList(pinList, pinNo)
    }

    override fun add(pinNo: Int) {
        pinList = addPinToList(boardPinList, pinList, pinNo)
    }
}

fun findPinElseThrowError(list: List<Pin>, pinNo: Int): Pin {
    return list.find { it.pinNo == pinNo } ?: throw Error("WTF")
}

fun addPinToList(boardPinList: List<Pin>, list: List<Pin>, pinNo: Int): List<Pin> {
    val arrayList = ArrayList(list)
    val pin = findPinElseThrowError(boardPinList, pinNo)
    if (!arrayList.contains(pin)) arrayList.add(pin)
    else throw Error("Already added")
    return arrayList
}

fun deletePinFromList(list: List<Pin>, pinNo: Int): List<Pin> {
    val pin = findPinElseThrowError(list, pinNo)
    val arrayList = ArrayList(list)
    arrayList.remove(pin)
    return arrayList
}

fun updatePin(list: List<Pin>, pinNo: Int, operation: Operation): Pin {
    return findPinElseThrowError(list, pinNo).copy(operation = operation)
}

fun replacePinOnList(list: List<Pin>, pin: Pin): List<Pin> {
    val oldPinIndex = list.indexOfFirst { it.pinNo == pin.pinNo }
    val arrayList = ArrayList(list)
    arrayList.removeAt(oldPinIndex)
    arrayList.add(oldPinIndex, pin)
    return arrayList
}

fun updatePinOnList(list: List<Pin>, pinNo: Int, operation: Operation): List<Pin> {
    val updatedPin = updatePin(list, pinNo, operation)
    return replacePinOnList(list, updatedPin)
}