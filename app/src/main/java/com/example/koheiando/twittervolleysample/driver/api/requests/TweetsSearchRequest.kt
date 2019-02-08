package com.example.koheiando.twittervolleysample.driver.api.requests

import com.android.volley.Request
import com.example.koheiando.twittervolleysample.driver.api.HttpRequest
import com.example.koheiando.twittervolleysample.driver.api.responses.TweetsSearchResponse
import com.example.koheiando.twittervolleysample.model.token.TwitterBearerToken
import com.example.koheiando.twittervolleysample.util.PreferenceUtil.UrlInfo.Companion.twitterHost
import org.json.JSONObject

class TweetsSearchRequest : HttpRequest() {
    companion object {
        private const val PATH = "1.1/search/tweets.json"
        private const val HEADER_AUTHORIZATION_KEY = "Authorization"
        private const val QUERY_KEY = "q"
    }

    override val method: Int = Request.Method.GET
    override val url: String = twitterHost + PATH
    override val retryCount: Int = 2

    suspend fun request(bearerToken: TwitterBearerToken, searchWords: String): TweetsSearchResponse {
        val queryParams: Map<String, String> = mapOf(QUERY_KEY to searchWords)
        return request(TweetsSearchResponse::class, headers(bearerToken), queryParams, JSONObject())
    }

    private fun headers(bearerToken: TwitterBearerToken): Map<String, String> {
        return mapOf(HEADER_AUTHORIZATION_KEY to bearerToken.toStringForHeader())
    }
}