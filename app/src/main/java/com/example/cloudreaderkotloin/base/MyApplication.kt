package com.example.cloudreaderkotloin.base

import android.app.Application
import android.util.Log
import com.example.cloudreaderkotloin.bussiness.common.utils.createCookieSharePreference
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.QbSdk.PreInitCallback

class MyApplication : Application() {
    companion object{
        var application: MyApplication? = null
        fun getContext(): MyApplication{
            return application!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        application = this

        initX5()
        createCookieSharePreference()
    }

    private fun  initX5() {
        //x5內核初始化回调
        val cb = object : PreInitCallback {
            override fun onViewInitFinished(p0: Boolean) {
                Log.i("app", " onViewInitFinished is ======== >>> $p0");
            }

            override fun onCoreInitFinished() {}
        }
        //x5内核初始化接口
        QbSdk.initX5Environment(this, cb);
    }

}