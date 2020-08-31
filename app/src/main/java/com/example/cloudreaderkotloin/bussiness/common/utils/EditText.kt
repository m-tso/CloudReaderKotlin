package com.example.cloudreaderkotloin.bussiness.common.utils

import android.text.Selection
import android.text.Spannable
import android.widget.EditText

fun setTextCursor(editText: EditText){
    val text = editText.text
    if (text is Spannable) {
        var spanText = text
        Selection.setSelection(spanText, text.length);
    }
}