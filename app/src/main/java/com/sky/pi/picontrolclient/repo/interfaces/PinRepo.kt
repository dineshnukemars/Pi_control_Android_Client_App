package com.sky.pi.picontrolclient.repo.interfaces

import com.sky.pi.picontrolclient.models.Pin

interface PinRepo {
    val pinList: ArrayList<Pin>

    fun replacePin(updatedPin: Pin)

    fun getPin(pinNo: Int): Pin

    fun deletePin(pinNo: Int)

    fun addPin(pinNo: Int)
}