package com.sky.pi.picontrolclient.adapter

import com.sky.pi.board.models.Operation

interface ItemActionListener {
    fun onDeletePin(pinNo: Int)
    fun onConfigurePin(pinNo: Int)
    fun onUpdatePin(pinNo: Int, operation: Operation)
}