package com.example.koheiando.twittervolleysample.views

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.koheiando.twittervolleysample.R
import com.example.koheiando.twittervolleysample.TvsApplication
import com.example.koheiando.twittervolleysample.model.Tweet

class TweetsRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var tweets: List<Tweet> = listOf()

    override fun getItemCount(): Int = tweets.size

    override fun onBindViewHolder(vh: RecyclerView.ViewHolder, position: Int) {
        Log.d("RecyclerViewAdapter", "onBindViewHolder position $position")
        tweets.getOrNull(position)?.let {
            if (vh is TweetViewHolder) {
                vh.bind(it)
                vh.setOnClickCallback {  } // maybe use it sometime
            }
        }
    }

    override fun onCreateViewHolder(vg: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d("RecyclerViewAdapter", "onCreateViewHolder")
        return TweetViewHolder(LayoutInflater.from(TvsApplication.getAppContext()).inflate(R.layout.tweet_item, vg, false))
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    fun updateTweets(newTweets: List<Tweet>) {
        Log.d("RecyclerViewAdapter", "updateTweets size ${newTweets.size}")
        tweets = newTweets
        DiffUtil.calculateDiff(DiffTweetsCallback(tweets, newTweets)).dispatchUpdatesTo(this)
    }
}

class DiffTweetsCallback(private val old: List<Tweet>, private val new: List<Tweet>) : DiffUtil.Callback() {

    /**
     * called first
     */
    override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return old[oldPosition].id == new[newPosition].id
    }

    /**
     * called after areItemsTheSame
     */
    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return false
    }

    override fun getNewListSize(): Int = new.size
    override fun getOldListSize(): Int = old.size
}