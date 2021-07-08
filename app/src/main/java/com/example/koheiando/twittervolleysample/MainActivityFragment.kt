package com.example.koheiando.twittervolleysample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.example.koheiando.twittervolleysample.driver.api.NetworkState
import com.example.koheiando.twittervolleysample.driver.api.requests.TweetsSearchRequest
import com.example.koheiando.twittervolleysample.driver.api.requests.TwitterBearerTokenRequest
import com.example.koheiando.twittervolleysample.model.token.TwitterBearerTokenRepository
import com.example.koheiando.twittervolleysample.model.tweet.TweetsRepository
import com.example.koheiando.twittervolleysample.util.getViewModel
import com.example.koheiando.twittervolleysample.viewModels.MainViewModel
import com.example.koheiando.twittervolleysample.views.TweetsRecyclerViewAdapter


/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : Fragment() {
    companion object {
        private const val IS_LOADING_KEY = "isLoading"
    }

    private lateinit var searchBox: EditText
    private lateinit var searchButton: Button
    private lateinit var progressCircle: ProgressBar
    private lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            isLoading = it.getBoolean(IS_LOADING_KEY, false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)?.apply {
            searchBox = findViewById<EditText>(R.id.search_box)
            searchButton = findViewById<Button>(R.id.search_btn)
            progressCircle = findViewById<ProgressBar>(R.id.progress_circle)
            recyclerView =
                findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recycler_view)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchButton.setOnClickListener {
            if (searchBox.text.isNotEmpty()) {
                fetchTweets(searchBox.text.toString())
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(activity)
            .apply { orientation = VERTICAL }
        recyclerView.adapter = TweetsRecyclerViewAdapter()

        updateUI(isLoading)

        // Add the tweets observer
        activity?.getViewModel {
            MainViewModel(
                TwitterBearerTokenRepository(TwitterBearerTokenRequest()),
                TweetsRepository(TweetsSearchRequest())
            )
        }?.tweetDataResults?.observe(this, Observer {
            it?.let { data ->
                when (data.state) {
                    NetworkState.SUCCESS -> {
                        (recyclerView.adapter as TweetsRecyclerViewAdapter).updateTweets(data.tweets)
                        updateUI(false)
                    }
                    NetworkState.LOADING -> {
                        updateUI(true)
                    }
                    NetworkState.NO_TOKEN -> {
                        updateUI(false)
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.add(R.id.popup_fragment_container, InitializeFragment.getInstance())
                            ?.commit()
                    }
                    NetworkState.ERROR -> {
                        updateUI(false)
                        Toast.makeText(activity, "ERROR....", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    /**
     * start the fetching process
     */
    private fun fetchTweets(searchWords: String) {
        activity?.getViewModel<MainViewModel>()?.search(searchWords)
    }

    /**
     * update the ui based on the loading status
     */
    private fun updateUI(doLoad: Boolean) {
        if (doLoad) {
            searchBox.isEnabled = false
            searchButton.isEnabled = false
            progressCircle.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            searchBox.isEnabled = true
            searchButton.isEnabled = true
            progressCircle.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
        isLoading = doLoad
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_LOADING_KEY, isLoading)
    }
}
