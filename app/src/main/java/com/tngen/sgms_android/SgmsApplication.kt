package com.tngen.sgms_android

import android.app.Application
import android.content.Context
import com.tngen.sgms_android.preferences.SharedPreference
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SgmsApplication :Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
        prefs = SharedPreference(applicationContext)
    }

    override fun onTerminate() {
        super.onTerminate()
        appContext = null
    }
    companion object {
        var appContext: Context? = null
            private set

        lateinit var prefs: SharedPreference

        var resumed: Boolean = false
        var logined: Boolean = false
    }
}