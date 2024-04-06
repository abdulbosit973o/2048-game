package com.example.a2048.app

import android.app.Application
import com.example.a2048.data.local.MyShared
import com.example.a2048.data.local.MySharedPreferences
import com.example.a2048.domain.AppController
import com.example.a2048.domain.AppRepositoryImpl
import com.example.a2048.settings.Settings

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Settings.init(this)
        AppController.init(this)
        AppRepositoryImpl.getInstance(this)
        MySharedPreferences.init(this)
    }
}