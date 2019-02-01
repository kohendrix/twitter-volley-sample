package com.example.koheiando.twittervolleysample.driver.api.responses

import com.example.koheiando.twittervolleysample.driver.api.HttpResponse
import com.example.koheiando.twittervolleysample.model.token.TwitterBearerToken
import org.json.JSONObject

class TwitterBearerTokenResponse : HttpResponse() {
    companion object {
        private const val ACCESS_TOKEN_KEY = "access_token"
    }

    lateinit var bearerToken: TwitterBearerToken

    override fun parseJson(json: JSONObject): HttpResponse {
        bearerToken = TwitterBearerToken(json.getString(ACCESS_TOKEN_KEY))
        return this
    }
}