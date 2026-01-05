package com.example.task1

import android.app.Application
import com.example.task1.utilities.BackgroundMusicPlayer
import com.example.task1.utilities.SharedPreferencesManager
import com.example.task1.utilities.SignalManager

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        SignalManager.init(this)
        SharedPreferencesManager.init(this)
        BackgroundMusicPlayer.init(this)
        BackgroundMusicPlayer.getInstance().setResourceId(R.raw.backgroundsound)
    }

}