package com.example.koheiando.twittervolleysample.driver.api

import org.json.JSONObject

abstract class HttpResponse {
    companion object {
        const val HEADER_KEY = "header"
        const val RESPONSE_KEY = "response"
    }

    internal abstract fun parseJson(json: JSONObject): HttpResponse
}