package com.example.koheiando.twittervolleysample.driver.api

import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.VolleyError

class HttpRetryPolicy(timeoutMs: Int = defaultTimeoutMs, maxRetry: Int = defaultMaxRetryCount) :
    DefaultRetryPolicy(timeoutMs, maxRetry, DEFAULT_BACKOFF_MULT) {
    companion object {
        private const val defaultTimeoutMs = 1000 * 10
        private const val defaultMaxRetryCount = 1
    }

    override fun retry(error: VolleyError?) {
        error?.also {
            Log.w("Http", "http connection error, Retrying... timeout ms: $currentTimeout count: $currentRetryCount")
            Log.w("HttpError", "request error : ${it.javaClass.name}")
            it.networkResponse?.let {
                Log.w("HttpError", "http connection error detail: ${error.networkResponse}")
            }
        }

        super.retry(error)
    }
}
