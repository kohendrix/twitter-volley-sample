package com.example.koheiando.twittervolleysample.model.token

import com.example.koheiando.twittervolleysample.driver.api.requests.TwitterBearerTokenRequest
import com.example.koheiando.twittervolleysample.util.PreferenceUtil.TwitterApiInfo.Companion.twitterBearerToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class TwitterBearerTokenRepository {
    suspend fun getToken(apiPub: String, apiSec: String): TwitterBearerToken {
        return if (twitterBearerToken.isEmpty()) {
            GlobalScope.async(Dispatchers.Default) { TwitterBearerTokenRequest(apiPub, apiSec).request().bearerToken }
                    .await()
        } else TwitterBearerToken(twitterBearerToken)
    }
}