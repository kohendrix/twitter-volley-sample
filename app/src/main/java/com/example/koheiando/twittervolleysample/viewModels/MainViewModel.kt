package com.example.koheiando.twittervolleysample.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.koheiando.twittervolleysample.model.token.TwitterBearerToken
import com.example.koheiando.twittervolleysample.model.token.TwitterBearerTokenRepository
import com.example.koheiando.twittervolleysample.model.tweet.Tweet
import com.example.koheiando.twittervolleysample.model.tweet.TweetDataResult
import com.example.koheiando.twittervolleysample.model.tweet.TweetsRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val repository = TweetsRepository()
    private val tweets = MutableLiveData<List<Tweet>>()

    fun getBearerToken(apiPub: String, apiSec: String,
                       onSuccess: (token: TwitterBearerToken) -> Unit,
                       onError: () -> Unit) {
        GlobalScope.launch {
            try {
                onSuccess(TwitterBearerTokenRepository().getToken(apiPub, apiSec))
            } catch (e: Exception) {
                Log.e(TAG, "getBearerToken", e)
                onError()
            }
        }
    }

    fun loadTweets(searchWords: String): LiveData<TweetDataResult> {
        return repository.loadTweets(searchWords)
    }

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }
}