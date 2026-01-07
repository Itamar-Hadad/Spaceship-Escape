package com.example.task1

import android.app.Application
import com.example.task1.utilities.BackgroundMusicPlayer
import com.example.task1.utilities.SharedPreferencesManager
import com.example.task1.utilities.SignalManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner


class App: Application(), DefaultLifecycleObserver {
    override fun onCreate() {
        super<Application>.onCreate()
        SignalManager.init(this)
        SharedPreferencesManager.init(this)
        BackgroundMusicPlayer.init(this)
        BackgroundMusicPlayer.getInstance().setResourceId(R.raw.backgroundsound)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

    }


    override fun onStop(owner: LifecycleOwner) {

        BackgroundMusicPlayer.getInstance().pauseMusic()
    }

    override fun onStart(owner: LifecycleOwner) {

        BackgroundMusicPlayer.getInstance().playIfAllowed()
    }

}