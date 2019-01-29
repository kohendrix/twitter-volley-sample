package com.example.koheiando.twittervolleysample.model.tweet

data class TweetDataResult(val state: NetworkState, val tweets: List<Tweet>, val exception: Exception? = null)

enum class NetworkState {
    SUCCESS,
    LOADING,
    NO_TOKEN,
    ERROR
}