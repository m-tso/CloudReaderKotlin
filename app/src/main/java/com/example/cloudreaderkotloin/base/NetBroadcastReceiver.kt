package com.example.cloudreaderkotloin.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.example.cloudreaderkotloin.bussiness.common.utils.checkConnectIsAvailable
import com.example.cloudreaderkotloin.bussiness.common.utils.getNetWorkState

/**
 * @Description: 网络变化监听器
 * @Author: tso 2020/8/31 14:25
 */
class NetBroadcastReceiver : BroadcastReceiver{
    lateinit var listener: NetChangeListener
    constructor(){

    }
    constructor(_listener: NetChangeListener){
        listener = _listener
    }

    override fun onReceive(context: Context?, intent: Intent) {
        // 如果相等的话就说明网络状态发生了变化
        if (intent.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            listener?.onChangeListener(checkConnectIsAvailable())
        }
    }

    // 自定义接口
    interface NetChangeListener {
        fun onChangeListener(connect: Boolean)
    }
}