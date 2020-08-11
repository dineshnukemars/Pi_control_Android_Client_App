package com.sky.pi.repo.board.layout

import com.sky.pi.board.models.Operation
import com.sky.pi.board.models.Pin
import com.sky.pi.collections.addPinToList
import com.sky.pi.collections.deletePinFromList
import com.sky.pi.collections.findElseThrow
import com.sky.pi.collections.updatePinOnList

class PinRepoImpl(private val boardPinList: List<Pin>) :
    PinRepo {
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
}