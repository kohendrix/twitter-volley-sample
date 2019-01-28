package com.example.koheiando.twittervolleysample.model

import java.util.*

data class Tweet(val id: Int, val text: String, val user: User, val createdAt: Date)