package com.example.koheiando.twittervolleysample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
import com.example.koheiando.twittervolleysample.util.PreferenceUtil.TwitterApiInfo.Companion.twitterBearerToken
import com.example.koheiando.twittervolleysample.util.clearFocusFromEditText

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate $savedInstanceState")
        setContentView(R.layout.activity_main)
        window.setSoftInputMode(SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        if (savedInstanceState == null) {
            if (twitterBearerToken.isEmpty()) {
                supportFragmentManager.beginTransaction()
                    .add(R.id.popup_fragment_container, InitializeFragment.getInstance())
                    .commit()
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        ev?.let { clearFocusFromEditText(ev) }
        return super.dispatchTouchEvent(ev)
    }
}
