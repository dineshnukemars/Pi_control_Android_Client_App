package com.sky.pi.client.libs

import android.util.Log

fun logD(message: String?) {
    Log.d("Raspi-Client", message ?: "null")
}

fun logErr(error: Throwable) {
    Log.e("Raspi-Client", error.toString(), error)
}
