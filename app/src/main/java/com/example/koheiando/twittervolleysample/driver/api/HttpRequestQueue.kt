package com.example.koheiando.twittervolleysample.driver.api

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley


/**
 * Singleton
 */
class HttpRequestQueue constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: HttpRequestQueue? = null

        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: HttpRequestQueue(context).also {
                    INSTANCE = it
                }
            }
    }

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    fun <T> addToRequestQueue(request: Request<T>) {
        requestQueue.add(request)
    }
}
