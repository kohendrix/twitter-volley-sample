package com.example.koheiando.twittervolleysample.model.tweet

import com.example.koheiando.twittervolleysample.model.User
import java.util.*

data class Tweet(val id: Int, val text: String, val user: User, val createdAt: Date)