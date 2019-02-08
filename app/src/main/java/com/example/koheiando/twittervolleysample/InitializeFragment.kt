package com.example.koheiando.twittervolleysample

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import com.example.koheiando.twittervolleysample.driver.api.NetworkState
import com.example.koheiando.twittervolleysample.driver.api.requests.TweetsSearchRequest
import com.example.koheiando.twittervolleysample.driver.api.requests.TwitterBearerTokenRequest
import com.example.koheiando.twittervolleysample.model.token.TwitterBearerTokenRepository
import com.example.koheiando.twittervolleysample.model.tweet.TweetsRepository
import com.example.koheiando.twittervolleysample.util.getViewModel
import com.example.koheiando.twittervolleysample.util.hideKeyboard
import com.example.koheiando.twittervolleysample.viewModels.MainViewModel

class InitializeFragment : androidx.fragment.app.Fragment() {
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
    private lateinit var vm: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("InitializeFragment", "onCreate $savedInstanceState")
        savedInstanceState?.apply {
            isLoading = getBoolean(KEY_IS_LOADING)
            isMessageError = getBoolean(KEY_IS_MESSAGE_ERROR)
        }
        vm = getViewModel {
            MainViewModel(
                TwitterBearerTokenRepository(TwitterBearerTokenRequest()),
                TweetsRepository(TweetsSearchRequest())
            )
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
        switchMessage(isMessageError)
        switchUI(isLoading)
        fetchTokenBtn.setOnClickListener {
            fetchToken()
        }
    }

    /**
     * call VM to fetch the token
     */
    private fun fetchToken() {
        val pubKey = apiKeyPubBox.text.toString()
        val secKey = apiKeySecBox.text.toString()
        if (arrayOf(pubKey, secKey).any { it.isEmpty() }) {
            switchMessage(true)
            return
        } else {
            activity?.apply {
                vm.getBearerToken(pubKey, secKey).observe(this, Observer {
                    when (it) {
                        NetworkState.SUCCESS -> runOnUiThread {
                            Toast.makeText(TvsApplication.getAppContext(), "Success", Toast.LENGTH_SHORT).show()
                            removeSelf()
                        }
                        NetworkState.ERROR, NetworkState.NO_TOKEN -> runOnUiThread {
                            switchUI(false)
                            switchMessage(true)
                        }
                        NetworkState.LOADING, null -> switchUI(true)
                    }
                })
            }
        }
    }

    /**
     * switches UI between loading <-> default
     * @param { Boolean } doLoad
     */
    private fun switchUI(doLoad: Boolean) {
        progressCircle.visibility = if (doLoad) View.VISIBLE else View.GONE
        message.visibility = if (doLoad) View.GONE else View.VISIBLE
        fetchTokenBtn.isEnabled = !doLoad
        apiKeyPubBox.isEnabled = !doLoad
        apiKeySecBox.isEnabled = !doLoad
        if (doLoad) {
            apiKeyPubBox.clearFocus()
            apiKeySecBox.clearFocus()
            view?.let { hideKeyboard(it) }
        }
        isLoading = doLoad
    }

    /**
     * switches the main message between error <-> default
     * @param { Boolean } isError
     */
    private fun switchMessage(isError: Boolean) {
        message.text =
                getText(if (isError) R.string.initialize_token_error_message else R.string.initialize_token_message)
        message.setTextColor(if (isError) Color.RED else Color.BLACK)
        isMessageError = isError
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