package com.sky.pi.picontrolclient

import android.app.Application
import com.sky.pi.picontrolclient.viewmodels.PinViewModel
import com.sky.pi.repo.impl.FakePiRepoImpl
import com.sky.pi.repo.impl.PinRepoImpl
import com.sky.pi.repo.interfaces.PiRepo
import com.sky.pi.repo.interfaces.PinRepo
import com.sky.pi.repo.models.PinLayout
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class PiApplication : Application() {

    private val myModule = module {
        single<PiRepo> {
//        PiAccessRepoImpl(
//            serverIp,
//            serverPort
//        )
            FakePiRepoImpl()
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
            androidContext(this@PiApplication)
            modules(myModule)
        }
    }
}