package com.example.cloudreaderkotloin.bussiness.common.utils

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.cloudreaderkotloin.base.MyApplication
import com.example.cloudreaderkotloin.bussiness.common.bean.NetWorkDataLoadState
import com.example.cloudreaderkotloin.bussiness.common.bean.NetWorkException
import kotlinx.coroutines.CoroutineExceptionHandler

/**
 * 没有网络
 */
private const val NETWORK_NONE = -1
/**
 * 移动网络
 */
private const val NETWORK_MOBILE = 0
/**
 * 无线网络
 */
private const val NETWORK_WIFI = 1

//判断是否联网
 fun checkConnectIsAvailable(context: Context? = null): Boolean {
    val conn =
        MyApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val net = conn.activeNetworkInfo
    val isAvailable = net != null && net.isConnected

    if (!isAvailable && context != null){
        Toast.makeText(context,NetWorkException.NOTCONNECT,Toast.LENGTH_SHORT).show()
    }
    return isAvailable
}

fun handelConnectExecption(loadState:MutableLiveData<NetWorkDataLoadState>): CoroutineExceptionHandler {
    return CoroutineExceptionHandler { _, e ->
        Log.i("错误", e.message)
        if (!checkConnectIsAvailable()) {
            loadState.value = NetWorkDataLoadState.Fail(NetWorkException.NOTCONNECT)
        }
    }
}



fun getNetWorkState(context: Context): Int {
    //得到连接管理器对象
    val connectivityManager = context
        .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager
        .activeNetworkInfo
    //如果网络连接，判断该网络类型
    if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
        if (activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI) {
            return NETWORK_WIFI //wifi
        } else if (activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE) {
            return NETWORK_MOBILE //mobile
        }
    } else {
        //网络异常
        return NETWORK_NONE
    }
    return NETWORK_NONE
}