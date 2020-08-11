package com.sky.pi.picontrolclient

import android.app.Application
import com.sky.pi.board.models.pi4bPinList
import com.sky.pi.picontrolclient.viewmodels.PinViewModel
import com.sky.pi.repo.board.layout.PinRepo
import com.sky.pi.repo.board.layout.PinRepoImpl
import com.sky.pi.repo.board.network.PiFakeRepoImpl
import com.sky.pi.repo.board.network.PiRepo
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class PiApplication : Application() {
    val serverIp = "192.168.0.16"
    val serverPort = 50053

    private val myModule = module {
        single<PiRepo> {
            PiFakeRepoImpl()
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