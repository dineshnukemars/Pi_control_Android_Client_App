package com.sky.pi.client.ui.adapter

import com.sky.pi.client.libs.models.Operation

interface ItemActionListener {
    fun onDeletePin(pinNo: Int)
    fun onConfigurePin(pinNo: Int)
    fun onUpdatePin(pinNo: Int, operation: Operation)
}