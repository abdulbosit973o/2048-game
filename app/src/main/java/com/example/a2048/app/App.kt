package com.example.a2048.app

import android.app.Application
import com.example.a2048.data.local.MyShared
import com.example.a2048.data.local.MySharedPreferences
import com.example.a2048.domain.AppRepositoryImpl

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppRepositoryImpl.getInstance(this)
        MySharedPreferences.init(this)
    }
}