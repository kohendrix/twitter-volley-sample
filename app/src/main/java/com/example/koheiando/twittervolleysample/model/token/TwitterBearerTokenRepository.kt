package com.example.koheiando.twittervolleysample.model.token

import android.util.Log
import com.example.koheiando.twittervolleysample.driver.api.NetworkState
import com.example.koheiando.twittervolleysample.driver.api.requests.TwitterBearerTokenRequest
import com.example.koheiando.twittervolleysample.util.PreferenceUtil.TwitterApiInfo.Companion.twitterBearerToken

class TwitterBearerTokenRepository(private val tokenRequest: TwitterBearerTokenRequest) {
    /**
     * this fetches the token but not update the preference value
     * if the preference value is not empty, returns it without validating
     * @param { String } apiPub : public key
     * @param { String } apiSec : secret key
     * @return { TwitterBearerTokenResult }
     */
    suspend fun getToken(apiPub: String, apiSec: String): TwitterBearerTokenResult {
        return if (twitterBearerToken.isNotEmpty()) {
            TwitterBearerTokenResult(
                NetworkState.SUCCESS,
                TwitterBearerToken(twitterBearerToken)
            )
        } else {
            getRefreshedToken(apiPub, apiSec)
        }
    }

    /**
     * this fetches the token but not update the preference value
     * always throw a request to the api regardless of preference value status
     * @param { String } apiPub : public key
     * @param { String } apiSec : secret key
     * @return { TwitterBearerTokenResult }
     */
    private suspend fun getRefreshedToken(
        apiPub: String,
        apiSec: String
    ): TwitterBearerTokenResult {
        return try {
            TwitterBearerTokenResult(
                NetworkState.SUCCESS,
                tokenRequest.request(apiPub, apiSec).bearerToken
            )
        } catch (e: Exception) {
            Log.e(TAG, "getRefreshedToken", e)
            TwitterBearerTokenResult(
                NetworkState.ERROR,
                TwitterBearerToken(""),
                e
            )
        }
    }

    companion object {
        private val TAG = TwitterBearerTokenRepository::class.java.simpleName
    }
}