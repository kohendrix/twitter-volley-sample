package com.example.koheiando.twittervolleysample.model.token

data class TwitterBearerToken(private val token: String) {
    override fun toString(): String = token
    fun toStringForHeader(): String = "Bearer $token"
    fun isEmpty() = token.isEmpty()
}