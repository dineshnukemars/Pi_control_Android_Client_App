package com.sky.pi.picontrolclient

import android.app.Application
import com.sky.pi.client.controller.pinrepo.PinRepo
import com.sky.pi.client.controller.pinrepo.PinRepoImpl
import com.sky.pi.client.controller.pirepo.RaspiFakeRepoImpl
import com.sky.pi.client.controller.pirepo.RaspiRepo
import com.sky.pi.client.controller.viewmodels.PinViewModel
import com.sky.pi.client.libs.models.pi4bPinList
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class PiApplication : Application() {
    val serverIp = "192.168.0.16"
    val serverPort = 50053

    private val myModule = module {
        single<RaspiRepo> {
            RaspiFakeRepoImpl()
        }
        single<PinRepo> {
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