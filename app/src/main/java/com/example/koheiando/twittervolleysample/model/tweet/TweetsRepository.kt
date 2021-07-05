package com.example.koheiando.twittervolleysample.model.tweet

import android.util.Log
import com.example.koheiando.twittervolleysample.driver.api.NetworkState
import com.example.koheiando.twittervolleysample.driver.api.requests.TweetsSearchRequest
import com.example.koheiando.twittervolleysample.model.User
import com.example.koheiando.twittervolleysample.model.token.TwitterBearerToken
import com.example.koheiando.twittervolleysample.util.PreferenceUtil.TwitterApiInfo.Companion.twitterBearerToken
import java.util.*

class TweetsRepository(private val tweetsSearchRequest: TweetsSearchRequest) {

    suspend fun loadTweets(searchWords: String): TweetDataResult {
        val token = TwitterBearerToken(twitterBearerToken)
        return if (token.isEmpty()) {
            TweetDataResult(NetworkState.NO_TOKEN, listOf())
        } else {
            try {
                TweetDataResult(
                    NetworkState.SUCCESS,
                    tweetsSearchRequest.request(token, searchWords).tweets
                )
            } catch (e: Exception) {
                Log.e("TweetsRepository", "loadTweets", e)
                TweetDataResult(NetworkState.ERROR, listOf(), e)
            }
        }
    }

    // returns dummy data
    private fun dummyTweets() = (0..15).map {
        Tweet(it, "DUMMY TWEET $it", User(it, "USER $it", "DUMMY DESC"), Date())
    }

    companion object {
        private val TAG = TweetsRepository::class.java.simpleName
    }
}