package com.sky.pi.picontrolclient

import android.app.Application
import com.sky.pi.client.controller.viewmodels.InfoViewModel
import com.sky.pi.client.controller.viewmodels.PinViewModel
import com.sky.pi.client.libs.models.pi4bPinList
import com.sky.pi.repo.pinrepo.PinRepo
import com.sky.pi.repo.pinrepo.PinRepoImpl
import com.sky.pi.repo.pirepo.RaspiFakeRepoImpl
import com.sky.pi.repo.pirepo.RaspiRepo

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
        viewModel {
            InfoViewModel()
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