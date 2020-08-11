package com.sky.pi.picontrolclient

import android.app.Application
import com.sky.pi.picontrolclient.viewmodels.PinViewModel
import com.sky.pi.repo.impl.FakePiRepoImpl
import com.sky.pi.repo.impl.PinRepoImpl
import com.sky.pi.repo.interfaces.IPiRepo
import com.sky.pi.repo.interfaces.IPinRepo
import com.sky.pi.repo.pi4bPinList
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class PiApplication : Application() {
    val serverIp = "192.168.0.16"
    val serverPort = 50053

    private val myModule = module {
        single<IPiRepo> {
//        PiAccessRepoImpl(
//            serverIp,
//            serverPort
//        )
            FakePiRepoImpl()
        }
        single<IPinRepo> {
            PinRepoImpl(pi4bPinList)
        }
        viewModel {
            PinViewModel(get(), get())
        }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PiApplication)
            modules(myModule)
        }
    }
}