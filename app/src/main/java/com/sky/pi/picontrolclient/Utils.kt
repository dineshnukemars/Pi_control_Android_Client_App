package com.sky.pi.picontrolclient

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.sky.pi.picontrolclient.repos.FakePiAccessRepoImpl
import com.sky.pi.picontrolclient.repos.PiAccessRepo
import com.sky.pi.picontrolclient.viewmodels.PinViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

const val serverIp = "192.168.0.16"
const val serverPort = 50053

fun ViewGroup.inflateLayout(@LayoutRes layoutId: Int): View {
    return LayoutInflater.from(context).inflate(layoutId, this, false)
}

val myModule = module {
    single<PiAccessRepo> {
//        PiAccessRepoImpl(
//            serverIp,
//            serverPort
//        )
        FakePiAccessRepoImpl(serverIp, serverPort)
    }
    viewModel {
        PinViewModel(get())
    }
}
