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

    override fun findPin(pinNo: Int): Pin = findElseThrow(pinList, pinNo)

    override fun delete(pinNo: Int) {
        pinList = deletePinFromList(pinList, pinNo)
    }

    override fun add(pinNo: Int) {
        pinList = addPinToList(boardPinList, pinList, pinNo)
    }

    private fun findElseThrow(list: List<Pin>, pinNo: Int): Pin =
        list.find { it.pinNo == pinNo } ?: throw Error("WTF")

    private fun addPinToList(boardPinList: List<Pin>, list: List<Pin>, pinNo: Int): List<Pin> {
        ifAlreadyExistThenThrow(list, pinNo)
        val mutableList = list.toMutableList()
        mutableList.add(findElseThrow(boardPinList, pinNo))
        return mutableList
    }

    private fun deletePinFromList(list: List<Pin>, pinNo: Int): List<Pin> =
        list.asSequence().filterNot { it.pinNo == pinNo }.toList()

    private fun updatePinOnList(list: List<Pin>, pinNo: Int, operation: Operation): List<Pin> =
        list.asSequence()
            .findAndUpdate(
                newItem = findElseThrow(list, pinNo).copy(operation = operation),
                predicate = { it.pinNo == pinNo })
            .toList()

    private fun ifAlreadyExistThenThrow(
        list: List<Pin>,
        pinNo: Int
    ) {
        val pin = list.find { it.pinNo == pinNo }
        if (pin != null) throw Error("WTF")
    }
}