package com.example.koheiando.twittervolleysample.viewModels

import android.util.Log
import androidx.lifecycle.*
import com.example.koheiando.twittervolleysample.driver.api.NetworkState
import com.example.koheiando.twittervolleysample.model.token.TwitterBearerTokenRepository
import com.example.koheiando.twittervolleysample.model.tweet.TweetDataResult
import com.example.koheiando.twittervolleysample.model.tweet.TweetsRepository
import com.example.koheiando.twittervolleysample.util.PreferenceUtil
import kotlinx.coroutines.Dispatchers

class MainViewModel(
    private val tokenRepository: TwitterBearerTokenRepository,
    private val tweetsRepository: TweetsRepository
) : ViewModel() {
    private val searchWords = MutableLiveData<String>() // trigger

    // tweet data exposed to Views
    val tweetDataResults: LiveData<TweetDataResult> =
        Transformations.switchMap(searchWords) { str ->
            liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
                emit(TweetDataResult(NetworkState.LOADING, listOf()))
                emit(tweetsRepository.loadTweets(str))
            }
        }

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
        return liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(NetworkState.LOADING)

            val tokenResultData = tokenRepository.getToken(apiPub, apiSec)
            if (tokenResultData.state == NetworkState.SUCCESS) {
                PreferenceUtil.TwitterApiInfo.twitterBearerToken =
                    tokenResultData.token.toString() // save it to local
            }
            if (tokenResultData.state == NetworkState.ERROR) {
                Log.e(TAG, "getBearerToken", tokenResultData.exception)
            }
            emit(tokenResultData.state)
        }
    }

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }
}