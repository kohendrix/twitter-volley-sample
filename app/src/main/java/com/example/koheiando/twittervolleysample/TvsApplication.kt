package com.example.koheiando.twittervolleysample

import android.app.Application
import android.content.Context

class TvsApplication : Application() {
    companion object {
        private var applicationContext: Context? = null
        fun getAppContext() =
            applicationContext ?: throw IllegalStateException("applicationContext has not been initialized.")
    }

    override fun onCreate() {
        super.onCreate()
        // initialize the app context
        TvsApplication.applicationContext = this.applicationContext
    }

    override fun onTerminate() {
        TvsApplication.applicationContext = null
        super.onTerminate()
    }
}
