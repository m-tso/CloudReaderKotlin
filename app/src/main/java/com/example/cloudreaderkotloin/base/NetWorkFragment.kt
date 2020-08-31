package com.example.cloudreaderkotloin.base

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

abstract class NetWorkFragment<VM:ViewModel,VDB: ViewDataBinding> : BaseFragment<VM, VDB>(),
    NetBroadcastReceiver.NetChangeListener {
    var listener: NetBroadcastReceiver.NetChangeListener? = null
    private var netBroadcastReceiver: NetBroadcastReceiver? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener = this
        //Android 7.0以上需要动态注册
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //实例化IntentFilter对象
            val filter = IntentFilter()
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
            netBroadcastReceiver = NetBroadcastReceiver(listener!!)
            //注册广播接收
            activity?.registerReceiver(netBroadcastReceiver, filter)
        }
    }

    override fun onChangeListener(connect: Boolean) {
        if (connect) onNetWorkConnect()
        else onNetWorkUnConnect()

    }

    //空方法，给子类实现，在网络连接上时
    open fun onNetWorkConnect() {

    }

    //空方法，给子类实现，在网络状态改变且为未连接时
   open fun onNetWorkUnConnect() {
        if (activity!! !is NetWorkActivity<*>){
            Toast.makeText(activity!!,"当前网络不可用",Toast.LENGTH_SHORT).show()
        }

    }

}