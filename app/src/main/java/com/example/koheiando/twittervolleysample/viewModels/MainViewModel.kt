package com.example.koheiando.twittervolleysample.viewModels

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.koheiando.twittervolleysample.model.TwitterBearerToken
import com.example.koheiando.twittervolleysample.model.TwitterBearerTokenRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    fun getBearerToken(
        apiPub: String, apiSec: String,
        onSuccess: (token: TwitterBearerToken) -> Unit, onError: () -> Unit
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