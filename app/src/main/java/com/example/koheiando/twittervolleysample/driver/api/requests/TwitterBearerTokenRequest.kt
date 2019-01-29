package com.example.koheiando.twittervolleysample.driver.api.requests

import android.util.Base64
import android.util.Log
import com.android.volley.Request
import com.example.koheiando.twittervolleysample.driver.api.HttpRequest
import com.example.koheiando.twittervolleysample.driver.api.responses.TwitterBearerTokenResponse
import com.example.koheiando.twittervolleysample.util.PreferenceUtil.UrlInfo.Companion.twitterHost

class TwitterBearerTokenRequest(private val apiPub: String, private val apiSec: String) : HttpRequest() {
    companion object {
        private const val PATH = "oauth2/token"
        private const val HEADER_AUTHORIZATION_KEY = "Authorization"
        private const val HEADER_CONTENT_TYPE_KEY = "Content-Type"
        private const val HEADER_CONTENT_TYPE_VAL = "application/x-www-form-urlencoded;charset=UTF-8"
    }

    override val method: Int = Request.Method.POST
    override val url: String = twitterHost + PATH
    override val queryParams: Map<String, String> = mapOf()
    private val body = "grant_type=client_credentials"

    suspend fun request(): TwitterBearerTokenResponse {
        return request(TwitterBearerTokenResponse::class, body)
    }

    override fun headers(): Map<String, String> {
        if (arrayOf(apiPub, apiSec).any { it.isEmpty() }) {
            throw IllegalStateException("Twitter Api Keys cannot be empty.")
        }

        val preEncoded = "$apiPub:$apiSec"
        val authValue = "Basic ${Base64.encodeToString(preEncoded.toByteArray(), Base64.NO_WRAP)}" // no line feed code

        Log.d(TAG, "auth value $authValue")

        return mapOf(
                HEADER_AUTHORIZATION_KEY to authValue,
                HEADER_CONTENT_TYPE_KEY to HEADER_CONTENT_TYPE_VAL
        )
    }
}