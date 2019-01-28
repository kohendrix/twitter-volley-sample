package com.example.koheiando.twittervolleysample.util

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.Rect
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.example.koheiando.twittervolleysample.TvsApplication


// fragment util
fun hideKeyboard(view: View) {
    (TvsApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(view.windowToken, 0)
}

// activity util
fun FragmentActivity.clearFocusFromEditText(event: MotionEvent) {
    if (event.action == MotionEvent.ACTION_DOWN) {
        currentFocus?.let { v ->
            if (v is EditText) {
                val rect = Rect().apply { v.getGlobalVisibleRect(this) }
                (!rect.contains(event.rawX.toInt(), event.rawY.toInt())).apply {
                    v.clearFocus()
                    hideKeyboard(v)
                }
            }
        }
    }
}

// view model getters
inline fun <reified T : ViewModel> Fragment.getViewModel() = ViewModelProviders.of(this).get(T::class.java)

inline fun <reified T : ViewModel> FragmentActivity.getViewModel() = ViewModelProviders.of(this).get(T::class.java)
