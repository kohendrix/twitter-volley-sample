package com.example.koheiando.twittervolleysample.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.koheiando.twittervolleysample.model.token.TwitterBearerToken
import com.example.koheiando.twittervolleysample.model.token.TwitterBearerTokenRepository
import com.example.koheiando.twittervolleysample.model.tweet.TweetDataResult
import com.example.koheiando.twittervolleysample.model.tweet.TweetsRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val repository = TweetsRepository()
    private val searchWords = MutableLiveData<String>()
    val tweetDataResults: LiveData<TweetDataResult> =
        Transformations.switchMap(searchWords) { str -> repository.loadTweets(str) }

    val data: LiveData<String> = Transformations.switchMap(searchWords) { s ->
        Log.d("MainViewModel", "data transformation")
        MutableLiveData<String>().apply { value = s }
    }

    fun search(str: String) {
        Log.d("MainViewModel", "search $str")
        searchWords.postValue(str)
    }

    fun getBearerToken(
        apiPub: String,
        apiSec: String,
        onSuccess: (token: TwitterBearerToken) -> Unit,
        onError: () -> Unit
    ) {
        GlobalScope.launch {
            try {
                onSuccess(TwitterBearerTokenRepository().getToken(apiPub, apiSec))
            } catch (e: Exception) {
                Log.e(TAG, "getBearerToken", e)
                onError()
            }
        }
    }

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }
}