package com.example.cloudreaderkotloin.bussiness.home.wan.model.retrofit

import com.example.cloudreaderkotloin.base.MyApplication
import com.example.cloudreaderkotloin.bussiness.home.wan.model.okhttp.intercepter.AddCookiesInterceptor
import com.example.cloudreaderkotloin.bussiness.home.wan.model.okhttp.intercepter.SaveCookiesInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitManager {
    private val baseUrl = "https://www.wanandroid.com"
    lateinit var mRetrofit: Retrofit
    lateinit var wanApiService: WanApiService
    private val connetTimeOut = 10000L

    init {
        createRetrofit()
        wanApiService = RetrofitManager.mRetrofit.create(WanApiService::class.java)
    }

    private fun createRetrofit() {
        val okHttpClient =
            OkHttpClient.Builder().connectTimeout(connetTimeOut, TimeUnit.MILLISECONDS)
                .addInterceptor(SaveCookiesInterceptor(MyApplication.getContext()))
                .addInterceptor(AddCookiesInterceptor(MyApplication.getContext())).build()

        mRetrofit = Retrofit.Builder().baseUrl(baseUrl).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}