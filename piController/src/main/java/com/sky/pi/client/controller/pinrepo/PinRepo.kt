package com.sky.pi.client.controller.pinrepo

import com.sky.pi.client.libs.models.Operation
import com.sky.pi.client.libs.models.Pin

interface PinRepo {

    fun pinList(): List<Pin>

    fun updateOperation(pinNo: Int, operation: Operation)

    fun findPin(pinNo: Int): Pin

    fun delete(pinNo: Int)

    fun add(pinNo: Int)
}