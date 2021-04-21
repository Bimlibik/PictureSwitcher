package ru.bimlibik.pictureswitcher

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class PictureSwitcherApp : Application() {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        // koin init
        startKoin {
            androidLogger()
            androidContext(this@PictureSwitcherApp)
            modules(appModule)
        }

        // realm init
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("favorite_pictures.realm")
            .build()
        Realm.setDefaultConfiguration(config)

        // Timber init
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    companion object {
        private lateinit var INSTANCE: PictureSwitcherApp

        @JvmStatic
        fun get(): PictureSwitcherApp = INSTANCE
    }
}