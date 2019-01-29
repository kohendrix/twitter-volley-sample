package com.example.koheiando.twittervolleysample.driver.api.responses

import android.util.Log
import com.example.koheiando.twittervolleysample.driver.api.HttpResponse
import com.example.koheiando.twittervolleysample.model.User
import com.example.koheiando.twittervolleysample.model.tweet.Tweet
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class TweetsSearchResponse : HttpResponse() {
    companion object {
        private const val STATUSES_KEY = "statuses"
        private const val CREATED_AT_KEY = "created_at"
        private const val TEXT_KEY = "text"
        private const val USER_KEY = "user"
        private const val ID_INT_KEY = "id"
        private const val NAME_KEY = "name"
        private const val DESCRIPTION_KEY = "description"
    }

    lateinit var tweets: List<Tweet>

    override fun parseJson(json: JSONObject): HttpResponse {
        val array = json.getJSONArray(STATUSES_KEY)
        Log.d("TweetsSearchResponse", "array size ${array.length()}")
        tweets = (0 until array.length()).map {
            val json = array.getJSONObject(it)
            val userJson = json.getJSONObject(USER_KEY)
            Tweet(
                    id = json.getInt(ID_INT_KEY),
                    text = json.getString(TEXT_KEY),
                    user = User(userJson.getInt(ID_INT_KEY), userJson.getString(NAME_KEY), userJson.getString(DESCRIPTION_KEY)),
                    createdAt = dateFromString(json.getString(CREATED_AT_KEY), "EEE MMM dd HH:mm:ss ZZZZ yyyy")
            )
        }

        return this
    }

    private fun dateFromString(dateStr: String, fmt: String): Date {
        return try {
            SimpleDateFormat(fmt, Locale.JAPAN).parse(dateStr)
        } catch (e: Exception) {
            e.printStackTrace()
            Date()
        }
    }
}