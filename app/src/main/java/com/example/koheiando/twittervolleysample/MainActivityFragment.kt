package com.example.koheiando.twittervolleysample

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.koheiando.twittervolleysample.driver.api.NetworkState
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
    private lateinit var recyclerView: RecyclerView
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            isLoading = it.getBoolean(IS_LOADING_KEY, false)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)?.apply {
            searchBox = findViewById<EditText>(R.id.search_box)
            searchButton = findViewById<Button>(R.id.search_btn)
            progressCircle = findViewById<ProgressBar>(R.id.progress_circle)
            recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
            activity?.getViewModel<MainViewModel>()?.let {

            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // activity should not be null but just in case
        activity?.getViewModel<MainViewModel>()?.tweetDataResults?.observe(this, Observer {
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
                        fragmentManager?.beginTransaction()
                                ?.add(R.id.popup_fragment_container, InitializeFragment.getInstance())?.commit()
                    }
                    NetworkState.ERROR -> {
                        updateUI(false)
                        Toast.makeText(activity, "ERROR....", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchButton.setOnClickListener {
            if (searchBox.text.isNotEmpty()) {
                fetchTweets(searchBox.text.toString())
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(activity).apply { orientation = LinearLayoutManager.VERTICAL }
        recyclerView.adapter = TweetsRecyclerViewAdapter()

        updateUI(isLoading)
    }

    /**
     * start the fetching process
     */
    private fun fetchTweets(searchWords: String) {
        updateUI(true)
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
