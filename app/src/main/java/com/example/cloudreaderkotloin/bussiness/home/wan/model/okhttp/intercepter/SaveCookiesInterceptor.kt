package com.example.cloudreaderkotloin.bussiness.home.wan.model.okhttp.intercepter

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log
import com.example.cloudreaderkotloin.bussiness.common.utils.COOKIE
import okhttp3.Interceptor
import okhttp3.Response


class SaveCookiesInterceptor : Interceptor {
    private lateinit var context: Context

    constructor(_context: Context){
        context = _context
    }
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.headers("set-cookie")?.isNotEmpty()!!){
            val cookies = response.headers("set-cookie")
            val cookie = encodeCookie(cookies)
            Log.i("encodecookie",cookie)
            cookie?.let { saveCookie(request.url().toString(),request.url().host(),cookie) }
        }
        return response
    }

    private fun encodeCookie(cookies: List<String>): String? {
        val sb = StringBuilder()
        val set: MutableSet<String> = HashSet()
        for (cookie in cookies) {
            val arr = cookie.split(";".toRegex()).toTypedArray()
            for (s in arr) {
                if (set.contains(s)) {
                    continue
                }
                set.add(s)
            }
        }
        for (cookie in set) {
            sb.append(cookie).append(";")
        }
        val last = sb.lastIndexOf(";")
        if (sb.length - 1 == last) {
            sb.deleteCharAt(last)
        }
        return sb.toString()
    }

    /**
     * 保存cookie到本地，这里我们分别为该url和host设置相同的cookie，其中host可选
     * 这样能使得该cookie的应用范围更广
     */
    private fun saveCookie(
        url: String,
        domain: String,
        cookies: String
    ) {
        val sp: SharedPreferences =
            context.getSharedPreferences(COOKIE, Context.MODE_PRIVATE)
        val editor = sp.edit()
        if (TextUtils.isEmpty(url)) {
            throw NullPointerException("url is null.")
        } else {
            editor.putString(url, cookies)
        }
        if (!TextUtils.isEmpty(domain)) {
            editor.putString(domain, cookies)
        }
        editor.apply()
    }

    /**
     * 清除本地Cookie
     *
     * @param context Context
     */
    fun clearCookie(context: Context) {
        val sp =
            context.getSharedPreferences(COOKIE, Context.MODE_PRIVATE)
        sp.edit().clear().apply()
    }
}