package com.example.koheiando.twittervolleysample.driver.api

import android.util.Log
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.example.koheiando.twittervolleysample.TvsApplication
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.reflect.KClass

abstract class HttpRequest {
    abstract val url: String
    abstract val method: Int
    abstract val retryCount: Int
    abstract fun headers(): Map<String, String>

    companion object {
        val TAG = this::class.java.simpleName
    }

    suspend fun <T : HttpResponse> request(responseClass: KClass<T>, jsonRequest: JSONObject?): HttpResponse {
        return suspendCancellableCoroutine { continuation ->
            val success = Response.Listener<JSONObject> { response ->
                continuation.resume(
                    responseClass.java.newInstance().parseJson(response)
                )
            }

            val error = Response.ErrorListener { error -> continuation.resumeWithException(error) }

            val request = object : JsonObjectRequest(method, url, jsonRequest, success, error) {
                override fun getHeaders(): Map<String, String> = headers()

                override fun parseNetworkError(volleyError: VolleyError): VolleyError {
                    return volleyError.networkResponse?.let {
                        Log.e(TAG, volleyError.networkResponse.headers.toString())
                        Log.e(TAG, String(volleyError.networkResponse.data))
                        VolleyError(String(volleyError.networkResponse.data))
                    } ?: volleyError
                }
            }

            request.retryPolicy = HttpRetryPolicy(maxRetry = retryCount)
            continuation.invokeOnCancellation { request.cancel() }

            try {
                HttpRequestQueue.getInstance(TvsApplication.getAppContext()).addToRequestQueue(request)
            } catch (e: Exception) {
                continuation.resumeWithException(e)
            }
        }
    }
}
