package com.sky.pi.picontrolclient.repo.interfaces

import com.sky.pi.picontrolclient.models.OperationData
import com.sky.pi.picontrolclient.models.Pin

interface PinRepo {

    fun pinList(): List<Pin>

    fun updateOperationData(pinNo: Int, operationData: OperationData): Pin

    fun getPin(pinNo: Int): Pin

    fun deletePin(pinNo: Int)

    fun addPin(pinNo: Int)
}