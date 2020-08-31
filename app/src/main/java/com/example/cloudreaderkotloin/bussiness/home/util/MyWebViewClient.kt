package com.example.cloudreaderkotloin.bussiness.home.util


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.View



import androidx.appcompat.app.AppCompatActivity

import com.example.cloudreaderkotloin.bussiness.common.utils.parseJs
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import me.jingbin.progress.WebProgress

class MyWebViewClient : WebViewClient {
    lateinit var progressBar: WebProgress
    lateinit var activity: AppCompatActivity

    constructor(_activity: AppCompatActivity,_progressBar: WebProgress): super(){
        this.progressBar = _progressBar
        this.activity = _activity
    }

    //处理err_unknown_url_scheme报错，http开头且不是下载apk的均允许继续
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if (TextUtils.isEmpty(url)) {
            return true
        }
        if (url?.contains("http")!! && !url.contains(".apk")) {
//            startActivity(activity, url)
            return false
        }
        return true
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        view?.visibility = View.INVISIBLE
        progressBar.show()
    }


    override fun onLoadResource(view: WebView?, url: String?) {
        getRidOfAd(view, url)
        super.onLoadResource(view, url)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        val runnable = Runnable { view?.visibility = View.VISIBLE }
        if (url?.contains("jianshu")!!) {
            view?.postDelayed(runnable, 500)
        } else {
            view?.visibility = View.VISIBLE
        }
        progressBar.hide()
    }



    fun startActivity(activity: Activity, url: String?) {
        try {
            val intent1 = Intent()
            intent1.action = "android.intent.action.VIEW"
            val uri = Uri.parse(url)
            intent1.data = uri
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intent1)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getRidOfAd(view: WebView?, url: String?) {
        if (url?.contains("jianshu")!!) {
            view?.loadUrl(view.context.parseJs("ad_filter/jianshu.js"))
            view?.loadUrl("javascript:hideTop()")
        }
    }



}