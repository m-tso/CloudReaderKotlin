package com.example.cloudreaderkotloin.base

import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.cloudreaderkotloin.base.NetBroadcastReceiver.NetChangeListener
import com.example.cloudreaderkotloin.bussiness.common.bean.NetWorkDataLoadState

/**
 * @Description: 网络相关的Activity的基类，提供网络变化和数据加载的相关接口
 * @Author: tso 2020/8/29 14:43
 */
abstract class NetWorkActivity<VM : NetWorkViewModel> : BaseActivity<VM>(), NetChangeListener {
    //网络加载状态类，该属性当前设计并不完美，后面可能去除
    var loadState: MutableLiveData<NetWorkDataLoadState>? = null
    //监听网络变化
    private var netBroadcastReceiver: NetBroadcastReceiver? = null
    var listener: NetChangeListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadState = viewModel?.ldLoadState

        loadState?.observe(this, Observer {
            when (it) {
                is NetWorkDataLoadState.Fail -> {
                    Toast.makeText(this, "${it.msg}", Toast.LENGTH_SHORT).show()
                }

                is NetWorkDataLoadState.Success -> {
                    onLoadSuccess()
                }

                is NetWorkDataLoadState.Loading -> {
                    onLoading()
                }
            }
        })

        listener = this
        //Android 7.0以上需要动态注册
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //实例化IntentFilter对象
            val filter = IntentFilter()
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
            netBroadcastReceiver = NetBroadcastReceiver(listener!!)
            //注册广播接收
            registerReceiver(netBroadcastReceiver, filter)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(netBroadcastReceiver)
    }

    override fun onChangeListener(connect: Boolean) {
        if (!connect) {
            onNetWorkUnConnect()
        } else {
            onNetWorkConnect()
        }
    }

    //空方法，给子类实现，在网络连接上时
    fun onNetWorkConnect() {

    }

    //空方法，给子类实现，在网络状态改变且为未连接时
    fun onNetWorkUnConnect() {
        if (isForeground()){
            Toast.makeText(this,"当前网络不可用",Toast.LENGTH_SHORT).show()
        }

    }

    fun onLoadSuccess() {

    }

    fun onLoading() {

    }


}