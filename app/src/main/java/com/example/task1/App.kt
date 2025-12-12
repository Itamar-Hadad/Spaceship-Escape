package com.example.task1

import android.app.Application
import com.example.task1.utilities.SignalManager

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        SignalManager.init(this)
    }
}