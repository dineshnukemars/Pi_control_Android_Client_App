package com.sky.pi.picontrolclient

import android.app.Application
import com.sky.pi.picontrolclient.models.PinLayout
import com.sky.pi.picontrolclient.repo.FakePiAccessRepoImpl
import com.sky.pi.picontrolclient.repo.PinRepoImpl
import com.sky.pi.picontrolclient.repo.interfaces.PiAccessRepo
import com.sky.pi.picontrolclient.repo.interfaces.PinRepo
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
            FakePiAccessRepoImpl()
        }
        single<PinRepo> {
            PinRepoImpl(get())
        }
        single {
            PinLayout()
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