package com.example.cloudreaderkotloin.bussiness.common.utils

import android.text.TextUtils

fun getWanArticleAuthor(author: String, shareName: String): String {
    var name: String = author
    if (TextUtils.isEmpty(name)) {
        name = shareName
    }
    if (TextUtils.isEmpty(name)) {
        name = "匿名"
    }
    return name

}