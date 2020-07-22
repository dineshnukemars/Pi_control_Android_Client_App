package com.sky.pi.picontrolclient

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.sky.pi.picontrolclient.repos.GrpcClientRepo
import com.sky.pi.picontrolclient.viewmodels.PinViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val serverIp = "192.168.0.16"
val serverPort = 50053

fun ViewGroup.inflateLayout(@LayoutRes layoutId: Int): View {
    return LayoutInflater.from(context).inflate(layoutId, this, false)
}

val myModule = module {
    single {
        GrpcClientRepo(
            serverIp,
            serverPort
        )
    }
    viewModel {
        PinViewModel(get())
    }
}

fun getGpioText(pinData: PinData): String {
    val builder = StringBuilder()
    builder.append("Pin no ${pinData.pinNo}  ")
    if (pinData.gpioNo < 0) {
        val gpioType = pinData.gpioType
        val type = when {
            gpioType.contains(GPIOType.GROUND) -> {
                GPIOType.GROUND.name
            }
            gpioType.contains(GPIOType.POWER_5V) -> {
                GPIOType.POWER_5V.name
            }
            gpioType.contains(GPIOType.POWER_3V) -> {
                GPIOType.POWER_3V.name
            }
            else -> ""
        }
        builder.append(type)
    } else {
        builder.append("GPIO - ${pinData.gpioNo}")
    }
    return builder.toString()
}