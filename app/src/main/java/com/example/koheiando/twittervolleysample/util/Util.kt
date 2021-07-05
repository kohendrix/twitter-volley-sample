package com.example.koheiando.twittervolleysample.util

import android.content.Context
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.koheiando.twittervolleysample.TvsApplication


// fragment util
fun hideKeyboard(view: View) {
    (TvsApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(view.windowToken, 0)
}

// activity util
fun androidx.fragment.app.FragmentActivity.clearFocusFromEditText(event: MotionEvent) {
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

class MyVMFactory<T>(val build: () -> T) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return build() as T
    }
}

// view model getters
inline fun <reified T : ViewModel> Fragment.getViewModel(noinline build: (() -> T)? = null): T {
    return if (build == null) ViewModelProvider(this).get(T::class.java)
    else ViewModelProvider(this, MyVMFactory(build)).get(T::class.java)
}

inline fun <reified T : ViewModel> FragmentActivity.getViewModel(noinline build: (() -> T)? = null): T {
    return if (build == null) ViewModelProvider(this).get(T::class.java)
    else ViewModelProvider(this, MyVMFactory(build)).get(T::class.java)
}