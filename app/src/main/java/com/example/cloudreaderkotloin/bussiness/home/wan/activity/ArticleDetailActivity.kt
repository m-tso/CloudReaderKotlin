package com.example.cloudreaderkotloin.bussiness.home.wan.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.KeyEvent
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.example.cloudreaderkotloin.R
import com.example.cloudreaderkotloin.base.BaseActivity
import com.example.cloudreaderkotloin.base.NetWorkActivity
import com.example.cloudreaderkotloin.bussiness.common.utils.setViewHighAsStatusBar
import com.example.cloudreaderkotloin.bussiness.home.util.MyWebChromClient
import com.example.cloudreaderkotloin.bussiness.home.util.MyWebViewClient
import com.example.cloudreaderkotloin.bussiness.home.wan.VmArticleDetail
import com.example.cloudreaderkotloin.bussiness.home.wan.bean.Article
import com.example.cloudreaderkotloin.databinding.ArticleDetailBinding
import kotlinx.android.synthetic.main.activity_article_detail.*


/**
 * @Description: 文章展示页，当前仅用于玩android文章的展示，后续可能会拓展其他的文章
 * @Author: tso 2020/8/12 10:30
 */

class ArticleDetailActivity(override val contentViewId: Int = R.layout.activity_article_detail) :
    NetWorkActivity<VmArticleDetail>() {
    private var articleLink: String? = null
    private var articleTile: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detail_content.fitsSystemWindows = false
        articleLink = intent.getStringExtra("link")
        articleTile = intent.getStringExtra("title")
        init()
    }

    private fun init(){
        setViewHighAsStatusBar(detail_stb_background)
        setSupportActionBar(detail_toobar)
        val actionBar = supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        detail_title.text = Html.fromHtml(articleTile)
        //调起跑马灯效果
        detail_title.postDelayed(Runnable { detail_title.isSelected = true }, 1900)

        detail_toobar.setNavigationOnClickListener { finish() }

        initWebView()
    }

    private fun initWebView(){

        WebView.setWebContentsDebuggingEnabled(true)
        val ws = detail_webview.settings

        load_progressbar.setColor(resources.getColor(R.color.colorTheme))
        detail_webview.webViewClient = MyWebViewClient(this,load_progressbar)
        detail_webview.webChromeClient = MyWebChromClient(load_progressbar)
        ws.javaScriptEnabled = true
        ws.domStorageEnabled = true

//        detail_webview.settingsExtension?.setDayOrNight(false);

        //设置允许从任何来源1加载内容，否则视频等数据无法加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        ws.textZoom = 100

        detail_webview.loadUrl(articleLink)
    }

    /**
     * @Description: ovverride on back event , go back to previous page if the webview has
     * @Author: tso 2020/8/12 10:23
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && detail_webview.canGoBack()) {
            // 返回上一页面
            detail_webview.settings.cacheMode = WebSettings.LOAD_NO_CACHE
            detail_webview.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}

