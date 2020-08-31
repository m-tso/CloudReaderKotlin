package com.example.cloudreaderkotloin.bussiness.common.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager

/**
 * 隐藏软键盘
 *
 * @param activity 要隐藏软键盘的activity
 */
fun hideSoftKeyBoard(activity: Activity) {
    val v = activity.window.peekDecorView()
    if (v != null && v.windowToken != null) {
        try {
            (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                activity.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
            )
        } catch (e: Exception) {
            Log.w("TAG", e.toString())
        }
    }
}