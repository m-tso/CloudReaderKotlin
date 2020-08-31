package com.example.cloudreaderkotloin.bussiness.common.utils

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.text.TextUtils
import com.example.cloudreaderkotloin.base.MyApplication
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val CONFIG = "config"
const val SEARCH_HISTORY = "search_history"
const val COOKIE = "cookie"
const val USER_NAME = "user_name"
const val PASSWORD = "password"
const val ARTICLE_COLLECTION = "article_collection"
const val WAN = "wan"
const val WAN_DOMAIN = "www.wanandroid.com"

val gson = Gson()

fun getSharePreferenceInstance(): SharedPreferences {
    return MyApplication.getContext().getSharedPreferences(CONFIG, MODE_PRIVATE)
}

fun createCookieSharePreference(){
    val sharedPreferences = MyApplication.getContext().getSharedPreferences(COOKIE, MODE_PRIVATE)
    if (!sharedPreferences.contains(WAN)){
        val editor = sharedPreferences.edit()
        editor.putString(WAN, WAN_DOMAIN)
        editor.apply()
    }
}

fun getCookieSharePreferenece(key: String): String? {
    val sharedPreferences = MyApplication.getContext().getSharedPreferences(COOKIE, MODE_PRIVATE)
    return sharedPreferences.getString(key,"")
}



fun saveString(key: String, data: String) {
    val sharedPreferencesEditor = getSharePreferenceInstance().edit()
    sharedPreferencesEditor.putString(key, data)
    sharedPreferencesEditor.apply()
}

fun getString(key: String): String? {
    return getSharePreferenceInstance().getString(key, "")

}

fun <T> saveArray(keyword: String, data: List<T>) {
    if (!TextUtils.isEmpty(keyword) && data.isNotEmpty()) {
        saveString(
            keyword,
            gson.toJson(data)
        )
    }
}

fun <T> getArray(key: String): ArrayList<T> {
    return try {
        val details: String = getString(key)!!
        if (!TextUtils.isEmpty(details)) {

            gson.fromJson<ArrayList<T>>(
                details,
                object : TypeToken<ArrayList<T?>?>() {}.type
            )
        } else {
            ArrayList()
        }
    } catch (e: Exception) {
        ArrayList()
    }
}

fun clearData(key: String) {
    val sharedPreferencesEditor = getSharePreferenceInstance().edit()
    sharedPreferencesEditor.remove(key)
    sharedPreferencesEditor.apply()
}

fun isLogin(): Boolean{
    val userName = getString(USER_NAME)
    val password = getString(PASSWORD)
    if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(PASSWORD)){
        return true
    }

    return false
}
