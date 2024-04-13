package com.example.a2048.app

import android.app.Application
import com.example.a2048.data.local.MySharedPreferences
import com.example.a2048.domain.AppRepository
import com.example.a2048.settings.Settings

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Settings.init(this)
        AppRepository.init(this)
        MySharedPreferences.init(this)
    }
}