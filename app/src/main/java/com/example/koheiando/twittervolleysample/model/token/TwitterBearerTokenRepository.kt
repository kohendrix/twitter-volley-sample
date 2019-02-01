package com.example.koheiando.twittervolleysample.model.token

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.koheiando.twittervolleysample.driver.api.NetworkState
import com.example.koheiando.twittervolleysample.driver.api.requests.TwitterBearerTokenRequest
import com.example.koheiando.twittervolleysample.util.PreferenceUtil.TwitterApiInfo.Companion.twitterBearerToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TwitterBearerTokenRepository {
    /**
     * this fetches the token but not update the preference value
     * if the preference value is not empty, returns it without validating
     * @param { String } apiPub : public key
     * @param { String } apiSec : secret key
     * @return { LiveData<TwitterBearerTokenResult> }
     */
    fun getToken(apiPub: String, apiSec: String): LiveData<TwitterBearerTokenResult> {
        val result = MutableLiveData<TwitterBearerTokenResult>()
        GlobalScope.launch(Dispatchers.Default) {
            try {
                result.postValue(TwitterBearerTokenResult(NetworkState.SUCCESS,
                        if (twitterBearerToken.isNotEmpty()) TwitterBearerToken(twitterBearerToken)
                        else TwitterBearerTokenRequest(apiPub, apiSec).request().bearerToken))
            } catch (e: Exception) {
                result.postValue(TwitterBearerTokenResult(NetworkState.ERROR, TwitterBearerToken(""), e))
                Log.e(TAG, "getRefreshedToken", e)
            }
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
    fun getRefreshedToken(apiPub: String, apiSec: String): LiveData<TwitterBearerTokenResult> {
        val result = MutableLiveData<TwitterBearerTokenResult>()
        GlobalScope.launch(Dispatchers.Default) {
            try {
                result.postValue(TwitterBearerTokenResult(NetworkState.SUCCESS, TwitterBearerTokenRequest(apiPub, apiSec).request().bearerToken))
            } catch (e: Exception) {
                result.postValue(TwitterBearerTokenResult(NetworkState.ERROR, TwitterBearerToken(""), e))
                Log.e(TAG, "getRefreshedToken", e)
            }
        }
        return result
    }

    companion object {
        private val TAG = TwitterBearerTokenRepository::class.java.simpleName
    }
}