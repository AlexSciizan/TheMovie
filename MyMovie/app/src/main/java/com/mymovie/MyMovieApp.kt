package com.mymovie

import android.content.Context
import androidx.multidex.MultiDexApplication
import androidx.multidex.MultiDex

class MyMovieApp : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
    }

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(context)
        MultiDex.install(context)
    }
}