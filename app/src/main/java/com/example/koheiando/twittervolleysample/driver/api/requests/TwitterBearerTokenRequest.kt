package com.example.koheiando.twittervolleysample.driver.api.requests

import android.util.Base64
import android.util.Log
import com.android.volley.Request
import com.example.koheiando.twittervolleysample.driver.api.HttpRequest
import com.example.koheiando.twittervolleysample.driver.api.responses.TwitterBearerTokenResponse
import com.example.koheiando.twittervolleysample.util.PreferenceUtil.UrlInfo.Companion.twitterHost

class TwitterBearerTokenRequest : HttpRequest() {
    companion object {
        private const val PATH = "oauth2/token"
        private const val HEADER_AUTHORIZATION_KEY = "Authorization"
        private const val HEADER_CONTENT_TYPE_KEY = "Content-Type"
        private const val HEADER_CONTENT_TYPE_VAL = "application/x-www-form-urlencoded;charset=UTF-8"
        private val TAG = TwitterBearerTokenRequest::class.java.simpleName
    }

    override val method: Int = Request.Method.POST
    override val url: String = twitterHost + PATH
    override val retryCount: Int = 2

    private val body = "grant_type=client_credentials"

    suspend fun request(apiPub: String, apiSec: String): TwitterBearerTokenResponse {
        return request(TwitterBearerTokenResponse::class, headers(apiPub, apiSec), mapOf(), body)
    }

    private fun headers(apiPub: String, apiSec: String): Map<String, String> {
        if (arrayOf(apiPub, apiSec).any { it.isEmpty() }) {
            throw IllegalStateException("Twitter Api Keys cannot be empty.")
        }

        val preEncoded = "$apiPub:$apiSec"
        val authValue = "Basic ${Base64.encodeToString(preEncoded.toByteArray(), Base64.NO_WRAP)}" // no line feed code

        Log.d(TAG, "preEncoded $preEncoded")
        Log.d(TAG, "auth value $authValue")

        return mapOf(
                HEADER_AUTHORIZATION_KEY to authValue,
                HEADER_CONTENT_TYPE_KEY to HEADER_CONTENT_TYPE_VAL
        )
    }
}