package com.example.koheiando.twittervolleysample

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : Fragment() {
    private lateinit var searchBox: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)?.apply {
            searchBox = findViewById<EditText>(R.id.search_box)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}
