package com.sky.pi.repo.pinrepo

import com.sky.pi.client.libs.collections.addPinToList
import com.sky.pi.client.libs.collections.deletePinFromList
import com.sky.pi.client.libs.collections.findElseThrow
import com.sky.pi.client.libs.collections.updatePinOnList
import com.sky.pi.client.libs.models.Operation
import com.sky.pi.client.libs.models.Pin


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