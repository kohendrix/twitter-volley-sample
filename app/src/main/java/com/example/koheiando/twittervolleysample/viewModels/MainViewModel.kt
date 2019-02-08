package com.example.koheiando.twittervolleysample.viewModels

import android.util.Log
import androidx.lifecycle.*
import com.example.koheiando.twittervolleysample.driver.api.NetworkState
import com.example.koheiando.twittervolleysample.model.token.TwitterBearerTokenRepository
import com.example.koheiando.twittervolleysample.model.token.TwitterBearerTokenResult
import com.example.koheiando.twittervolleysample.model.tweet.TweetDataResult
import com.example.koheiando.twittervolleysample.model.tweet.TweetsRepository
import com.example.koheiando.twittervolleysample.util.PreferenceUtil

class MainViewModel(
    private val tokenRepository: TwitterBearerTokenRepository,
    private val tweetsRepository: TweetsRepository
) : ViewModel() {
    private val searchWords = MutableLiveData<String>() // trigger

    // tweet data exposed to Views
    val tweetDataResults: LiveData<TweetDataResult> =
        Transformations.switchMap(searchWords) { str -> tweetsRepository.loadTweets(str) }

    /**
     * search trigger
     */
    fun search(str: String) {
        Log.d(TAG, "search $str")
        searchWords.value = str
    }

    /**
     * the views don't really have to know the token
     * they just need to know if we got it or not
     * @param { String } apiPub : public key
     * @param { String } apiSec : secret key
     * @return { LiveData<NetworkState> }
     */
    fun getBearerToken(apiPub: String, apiSec: String): LiveData<NetworkState> {
        val mediator = MediatorLiveData<NetworkState>()
        val tokenResultData = tokenRepository.getToken(apiPub, apiSec)

        mediator.addSource(tokenResultData) { result: TwitterBearerTokenResult? ->
            result?.let {
                if (result.state == NetworkState.SUCCESS) {
                    PreferenceUtil.TwitterApiInfo.twitterBearerToken = result.token.toString() // save it to local
                }
                if (result.state == NetworkState.ERROR) {
                    Log.e(TAG, "getBearerToken", result.exception)
                }
                mediator.postValue(result.state)
            }
        }
        return mediator
    }

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }
}