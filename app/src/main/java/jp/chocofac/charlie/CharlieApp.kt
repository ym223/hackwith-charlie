package jp.chocofac.charlie

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class CharlieApp : Application() {
    override fun onCreate() {
        super.onCreate()

//        if (BuildConfig.DEBUG) {
            // Timber.d("{Your Message}")でログに出力できる
            Timber.plant(Timber.DebugTree())
//        }
    }
}