package com.sky.pi.repo.interfaces

import com.sky.pi.repo.models.Pin

interface IPinLayout {
    fun pinForPinNo(pinNo: Int): Pin
}