package com.example.koheiando.twittervolleysample

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.example.koheiando.twittervolleysample.util.PreferenceUtil

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
        try {
            PreferenceUtil.setContext(this.applicationContext)
        } catch (e: Exception) {
            Log.e("TvsApplication", e.toString())
        }
    }

    override fun onTerminate() {
        TvsApplication.applicationContext = null
        super.onTerminate()
    }

}

inline fun <reified T : ViewModel> Fragment.getViewModel() = ViewModelProviders.of(this).get(T::class.java)

inline fun <reified T : ViewModel> FragmentActivity.getViewModel() = ViewModelProviders.of(this).get(T::class.java)

