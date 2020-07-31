package com.sky.pi.picontrolclient.adapters

import com.sky.pi.repo.models.Operation

interface ItemActionListener {
    fun onDeletePin(pinNo: Int)
    fun onConfigurePin(pinNo: Int)
    fun onUpdatePin(pinNo: Int, operation: Operation)
}