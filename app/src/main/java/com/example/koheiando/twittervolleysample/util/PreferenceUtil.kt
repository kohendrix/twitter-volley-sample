package com.example.koheiando.twittervolleysample.util

import android.content.Context
import android.preference.PreferenceManager
import com.example.koheiando.twittervolleysample.R
import org.json.JSONArray
import java.io.IOException

class PreferenceUtil {
    companion object {
        private const val prefName = "default"
        private lateinit var appContext: Context

        fun setContext(context: Context) {
            appContext = context.applicationContext
            PreferenceManager.setDefaultValues(
                context.applicationContext,
                prefName,
                Context.MODE_PRIVATE,
                R.xml.default_values,
                false
            )
        }

        fun clear(name: String) =
            appContext.getSharedPreferences(name, Context.MODE_PRIVATE).edit().clear().apply()

        fun getBoolean(name: String, key: String, default: Boolean = false): Boolean =
            appContext.getSharedPreferences(name, Context.MODE_PRIVATE).getBoolean(key, default)

        fun setBoolean(name: String, key: String, value: Boolean) =
            appContext.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putBoolean(key, value).apply()

        fun getString(name: String, key: String, default: String = ""): String =
            appContext.getSharedPreferences(name, Context.MODE_PRIVATE).getString(key, default)

        fun setString(name: String, key: String, value: String) =
            appContext.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putString(key, value).apply()

        fun setSyncString(name: String, key: String, value: String) {
            val isSuccess =
                appContext.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putString(key, value).commit()
            if (!isSuccess) throw IOException("failed  write preference name: $name  value: $value")
        }

        fun removeString(name: String, key: String) =
            appContext.getSharedPreferences(name, Context.MODE_PRIVATE).edit().remove(key).apply()

        fun setSyncStrings(name: String, keyValues: Map<String, String>) {
            val isSuccess = appContext.getSharedPreferences(name, Context.MODE_PRIVATE).edit().apply {
                keyValues.forEach {
                    this.putString(it.key, it.value)
                }
            }.commit()

            if (!isSuccess) throw IOException("failed  write preference names: $name  values: $keyValues")
        }

        fun getInt(name: String, key: String, default: Int = 0): Int =
            appContext.getSharedPreferences(name, Context.MODE_PRIVATE).getInt(key, default)

        fun setInt(name: String, key: String, value: Int) =
            appContext.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putInt(key, value).apply()

        fun getJson(name: String, key: String, default: JSONArray = JSONArray()): JSONArray =
            JSONArray(appContext.getSharedPreferences(name, Context.MODE_PRIVATE).getString(key, default.toString()))

        fun setJson(name: String, key: String, value: JSONArray) =
            appContext.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putString(key, value.toString()).apply()
    }

    class UrlInfo {
        companion object {
            var twitterHost: String
                get() = getString(prefName, "twitterHost")
                set(value) = setString(prefName, "twitterHost", value) // probably not used but can be rewritten
        }
    }

    class TwitterApiInfo {
        companion object {
            var twitterBearerToken: String
                get() = getString(prefName, "twitterBearerToken")
                set(value) = setString(prefName, "twitterBearerToken", value)
        }
    }
}