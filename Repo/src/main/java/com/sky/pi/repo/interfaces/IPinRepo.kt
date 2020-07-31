package com.sky.pi.repo.interfaces

import com.sky.pi.repo.models.Operation
import com.sky.pi.repo.models.Pin

interface IPinRepo {

    fun pinList(): List<Pin>

    fun updateOperation(pinNo: Int, operation: Operation): Pin

    fun pinForNo(pinNo: Int): Pin

    fun delete(pinNo: Int)

    fun add(pinNo: Int)
}