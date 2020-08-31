package com.example.cloudreaderkotloin.bussiness.home.util



import android.webkit.WebView
import android.widget.ProgressBar
import com.tencent.smtt.sdk.WebChromeClient

import me.jingbin.progress.WebProgress

class MyWebChromClient: WebChromeClient {
    lateinit var progressBar: WebProgress

    constructor(_progressBar: WebProgress):super(){
        this.progressBar = _progressBar
    }
    override fun onProgressChanged(p0: com.tencent.smtt.sdk.WebView?, newProgress: Int) {
       if (newProgress != 100){
           progressBar.setProgress(newProgress)
       }
    }

}