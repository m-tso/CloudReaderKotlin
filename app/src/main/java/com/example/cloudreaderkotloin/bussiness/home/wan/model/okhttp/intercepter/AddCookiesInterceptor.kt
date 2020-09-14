package com.example.cloudreaderkotloin.bussiness.home.wan.model.okhttp.intercepter

import android.content.Context
import android.text.TextUtils
import com.example.cloudreaderkotloin.bussiness.common.utils.*
import okhttp3.Interceptor
import okhttp3.Response


class AddCookiesInterceptor : Interceptor {
    private var context: Context
    constructor(_context: Context){
        context = _context
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()

        val userName = getString(USER_NAME)
        val password = getString(PASSWORD)
        val wanCookie = getCookieSharePreferenece(WAN)

        if (!TextUtils.isEmpty(wanCookie) && request.url().host() == wanCookie){
            if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)){
                builder.addHeader("Cookie","loginUserName=$userName")
                builder.addHeader("Cookie","loginUserPassword=$password")
            }
        }


        return chain.proceed(builder.build())
    }




}