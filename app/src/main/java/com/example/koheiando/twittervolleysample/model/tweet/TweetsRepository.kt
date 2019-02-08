package com.example.koheiando.twittervolleysample.model.tweet

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.koheiando.twittervolleysample.driver.api.NetworkState
import com.example.koheiando.twittervolleysample.driver.api.requests.TweetsSearchRequest
import com.example.koheiando.twittervolleysample.model.User
import com.example.koheiando.twittervolleysample.model.token.TwitterBearerToken
import com.example.koheiando.twittervolleysample.util.PreferenceUtil.TwitterApiInfo.Companion.twitterBearerToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class TweetsRepository(val tweetsSearchRequest: TweetsSearchRequest) {

    fun loadTweets(searchWords: String): LiveData<TweetDataResult> {
        val token = TwitterBearerToken(twitterBearerToken)
        val dataResult = MutableLiveData<TweetDataResult>()
        dataResult.postValue(TweetDataResult(NetworkState.LOADING, listOf()))
        if (token.isEmpty()) {
            dataResult.postValue(TweetDataResult(NetworkState.NO_TOKEN, listOf()))
        } else {
            // todo switch to data source sometime
            GlobalScope.launch(Dispatchers.Default) {
                try {
                    dataResult.postValue(
                        TweetDataResult(
                            NetworkState.SUCCESS,
                            tweetsSearchRequest.request(token, searchWords).tweets
                        )
                    )
                } catch (e: Exception) {
                    dataResult.postValue(TweetDataResult(NetworkState.ERROR, listOf(), e))
                    Log.e("TweetsRepository", "loadTweets", e)
                }
            }
        }
        return dataResult
    }

    // returns dummy data
    private fun dummyTweets() = (0..15).map {
        Tweet(it, "DUMMY TWEET $it", User(it, "USER $it", "DUMMY DESC"), Date())
    }

    companion object {
        private val TAG = TweetsRepository::class.java.simpleName
    }
}