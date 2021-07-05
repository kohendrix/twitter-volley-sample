package com.example.koheiando.twittervolleysample.model.token

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.koheiando.twittervolleysample.driver.api.NetworkState
import com.example.koheiando.twittervolleysample.driver.api.requests.TwitterBearerTokenRequest
import com.example.koheiando.twittervolleysample.util.PreferenceUtil.TwitterApiInfo.Companion.twitterBearerToken

class TwitterBearerTokenRepository(private val tokenRequest: TwitterBearerTokenRequest) {
    /**
     * this fetches the token but not update the preference value
     * if the preference value is not empty, returns it without validating
     * @param { String } apiPub : public key
     * @param { String } apiSec : secret key
     * @return { LiveData<TwitterBearerTokenResult> }
     */
    suspend fun getToken(apiPub: String, apiSec: String): LiveData<TwitterBearerTokenResult> {
        val result = MutableLiveData<TwitterBearerTokenResult>()
        if (twitterBearerToken.isNotEmpty()) {
            result.postValue(
                TwitterBearerTokenResult(
                    NetworkState.SUCCESS,
                    TwitterBearerToken(twitterBearerToken)
                )
            )
        } else {
            val res = getRefreshedToken(apiPub, apiSec)
            result.postValue(res)
        }
        return result
    }

    /**
     * this fetches the token but not update the preference value
     * always throw a request to the api regardless of preference value status
     * @param { String } apiPub : public key
     * @param { String } apiSec : secret key
     * @return { LiveData<TwitterBearerTokenResult> }
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