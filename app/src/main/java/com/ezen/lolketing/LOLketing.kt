package com.ezen.lolketing

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LOLketing : Application() {
    companion object {
        private lateinit var application: LOLketing
        fun getInstance() : LOLketing = application
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }
}