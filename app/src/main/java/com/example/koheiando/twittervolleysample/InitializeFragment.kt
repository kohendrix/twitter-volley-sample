package com.example.koheiando.twittervolleysample

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView


class InitializeFragment : Fragment() {
    private lateinit var apiKeyPubBox: EditText
    private lateinit var apiKeySecBox: EditText
    private lateinit var fetchTokenBtn: Button
    private lateinit var message: TextView
    private lateinit var progressCircle: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_initialize, container, false)?.apply {
            apiKeyPubBox = findViewById<EditText>(R.id.api_key_pub_box)
            apiKeySecBox = findViewById<EditText>(R.id.api_key_sec_box)
            fetchTokenBtn = findViewById<Button>(R.id.fetch_token_btn)
            message = findViewById<TextView>(R.id.fetch_token_message)
            progressCircle = findViewById<ProgressBar>(R.id.loading_progress)

            setOnTouchListener { v, event -> true }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchTokenBtn.setOnClickListener {
            fetchToken()
        }
    }

    private fun fetchToken() {
        val pubKey = apiKeyPubBox.text.toString()
        val secKey = apiKeySecBox.text.toString()
        if (arrayOf(pubKey, secKey).any { it.isEmpty() }) {
            message.text = getText(R.string.initialize_token_error_message)
            message.setTextColor(Color.RED)
            return
        } else {
            updateUI(true)
        }
    }

    private fun updateUI(isLoading: Boolean) {
        progressCircle.visibility = if (isLoading) View.VISIBLE else View.GONE
        message.visibility = if (isLoading) View.GONE else View.VISIBLE
        fetchTokenBtn.isEnabled = !isLoading
        apiKeyPubBox.isFocusable = !isLoading
        apiKeySecBox.isFocusable = !isLoading
        if (isLoading) {
            view?.let { hideKeyboard(it) }
        }
    }

    private fun hideKeyboard(view: View) {
        (TvsApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(view.windowToken, 0)
    }
}