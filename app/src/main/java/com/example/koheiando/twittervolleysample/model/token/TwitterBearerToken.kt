package com.example.koheiando.twittervolleysample.model.token

import com.example.koheiando.twittervolleysample.driver.api.NetworkState

data class TwitterBearerToken(private val token: String) {
    override fun toString(): String = token
    fun toStringForHeader(): String = "Bearer $token"
    fun isEmpty() = token.isEmpty()
}

data class TwitterBearerTokenResult(val state: NetworkState, val token: TwitterBearerToken, val exception: Exception? = null)