package com.example.koheiando.twittervolleysample

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.koheiando.twittervolleysample.viewModels.MainViewModel


class InitializeFragment : Fragment() {
    companion object {
        fun getInstance(b: Bundle? = null) = InitializeFragment().apply {
            b?.let { arguments = it }
        }

        private const val KEY_IS_LOADING = "isLoading"
        private const val KEY_IS_MESSAGE_ERROR = "isMessageError"
    }

    private lateinit var apiKeyPubBox: EditText
    private lateinit var apiKeySecBox: EditText
    private lateinit var fetchTokenBtn: Button
    private lateinit var message: TextView
    private lateinit var progressCircle: ProgressBar
    private var isLoading = false
    private var isMessageError = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("InitializeFragment", "onCreate $savedInstanceState")
        savedInstanceState?.apply {
            isLoading = getBoolean(KEY_IS_LOADING)
            isMessageError = getBoolean(KEY_IS_MESSAGE_ERROR)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_initialize, container, false)?.apply {
            apiKeyPubBox = findViewById<EditText>(R.id.api_key_pub_box)
            apiKeySecBox = findViewById<EditText>(R.id.api_key_sec_box)
            fetchTokenBtn = findViewById<Button>(R.id.fetch_token_btn)
            message = findViewById<TextView>(R.id.fetch_token_message)
            progressCircle = findViewById<ProgressBar>(R.id.loading_progress)

            setOnTouchListener { _, _ -> true }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateMessage(isMessageError)
        updateUI(isLoading)
        fetchTokenBtn.setOnClickListener {
            fetchToken()
        }
    }

    private fun fetchToken() {
        val pubKey = apiKeyPubBox.text.toString()
        val secKey = apiKeySecBox.text.toString()
        if (arrayOf(pubKey, secKey).any { it.isEmpty() }) {
            updateMessage(true)
            return
        } else {
            activity?.let { _activity ->
                updateUI(true)
                _activity.getViewModel<MainViewModel>().getBearerToken(
                    pubKey, secKey,
                    {
                        _activity.runOnUiThread {
                            Toast.makeText(TvsApplication.getAppContext(), "Success", Toast.LENGTH_SHORT).show()
                            removeSelf()
                        }
                    },
                    {
                        _activity.runOnUiThread {
                            updateUI(false)
                            updateMessage(true)
                        }
                    }
                )
            }
        }
    }

    private fun updateUI(doLoad: Boolean) {
        progressCircle.visibility = if (doLoad) View.VISIBLE else View.GONE
        message.visibility = if (doLoad) View.GONE else View.VISIBLE
        fetchTokenBtn.isEnabled = !doLoad
        apiKeyPubBox.isFocusable = !doLoad
        apiKeySecBox.isFocusable = !doLoad
        if (doLoad) {
            view?.let { hideKeyboard(it) }
        }
        isLoading = doLoad
    }

    private fun updateMessage(isError: Boolean) {
        message.text =
                getText(if (isError) R.string.initialize_token_error_message else R.string.initialize_token_message)
        message.setTextColor(if (isError) Color.RED else Color.BLACK)
        isMessageError = isError
    }

    private fun hideKeyboard(view: View) {
        (TvsApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun removeSelf() {
        fragmentManager?.beginTransaction()?.remove(this)?.commit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_IS_LOADING, isLoading)
        outState.putBoolean(KEY_IS_MESSAGE_ERROR, isMessageError)
    }
}