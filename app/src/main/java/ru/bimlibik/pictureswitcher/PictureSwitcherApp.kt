package ru.bimlibik.pictureswitcher

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class PictureSwitcherApp : Application() {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        startKoin {
            androidLogger()
            androidContext(this@PictureSwitcherApp)
            modules(appModule)
        }
    }

    companion object {
        private lateinit var INSTANCE: PictureSwitcherApp

        @JvmStatic
        fun get(): PictureSwitcherApp = INSTANCE
    }
}