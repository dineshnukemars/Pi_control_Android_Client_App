package com.sky.pi.repo.board.layout

import com.sky.pi.board.models.Operation
import com.sky.pi.board.models.Pin

interface PinRepo {

    fun pinList(): List<Pin>

    fun updateOperation(pinNo: Int, operation: Operation)

    fun findPin(pinNo: Int): Pin

    fun delete(pinNo: Int)

    fun add(pinNo: Int)
}