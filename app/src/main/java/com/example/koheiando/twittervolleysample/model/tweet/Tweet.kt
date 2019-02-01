package com.example.koheiando.twittervolleysample.model.tweet

import com.example.koheiando.twittervolleysample.driver.api.NetworkState
import com.example.koheiando.twittervolleysample.model.User
import java.util.*

data class Tweet(val id: Int, val text: String, val user: User, val createdAt: Date)

data class TweetDataResult(val state: NetworkState, val tweets: List<Tweet>, val exception: Exception? = null)