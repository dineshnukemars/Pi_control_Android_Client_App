package com.sky.pi.picontrolclient

import android.app.Application
import com.sky.pi.picontrolclient.repos.FakePiAccessRepoImpl
import com.sky.pi.picontrolclient.repos.PiAccessRepo
import com.sky.pi.picontrolclient.repos.PinRepo
import com.sky.pi.picontrolclient.viewmodels.PinViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {

    private val myModule = module {
        single<PiAccessRepo> {
//        PiAccessRepoImpl(
//            serverIp,
//            serverPort
//        )
            FakePiAccessRepoImpl(serverIp, serverPort)
        }
        single {
            PinRepo()
        }
        viewModel {
            PinViewModel(get(), get())
        }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(myModule)
        }
    }
}