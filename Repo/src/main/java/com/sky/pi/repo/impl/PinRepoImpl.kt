package com.sky.pi.repo.impl

import com.sky.pi.repo.addPinToList
import com.sky.pi.repo.deletePinFromList
import com.sky.pi.repo.findElseThrow
import com.sky.pi.repo.interfaces.IPinRepo
import com.sky.pi.repo.models.Operation
import com.sky.pi.repo.models.Pin
import com.sky.pi.repo.updatePinOnList

class PinRepoImpl(private val boardPinList: List<Pin>) : IPinRepo {
    private var pinList: List<Pin> = ArrayList()

    override fun pinList(): List<Pin> = pinList

    override fun updateOperation(
        pinNo: Int,
        operation: Operation
    ) {
        pinList = updatePinOnList(pinList, pinNo, operation)
    }

    override fun findPin(pinNo: Int): Pin =
        findElseThrow(pinList, pinNo)

    override fun delete(pinNo: Int) {
        pinList = deletePinFromList(pinList, pinNo)
    }

    override fun add(pinNo: Int) {
        pinList = addPinToList(boardPinList, pinList, pinNo)
    }
}