package com.sky.pi.picontrolclient.repo.interfaces

import com.sky.pi.picontrolclient.models.Operation
import com.sky.pi.picontrolclient.models.Pin

interface PinRepo {

    fun pinList(): List<Pin>

    fun updateOperation(pinNo: Int, operation: Operation): Pin

    fun pinForNo(pinNo: Int): Pin

    fun delete(pinNo: Int)

    fun add(pinNo: Int)
}