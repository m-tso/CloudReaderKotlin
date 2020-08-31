package com.example.cloudreaderkotloin.bussiness.common.bean
/**
 * @Description: 网络数据加载状态类
 * @Author: tso 2020/8/29 14:48
 * @param msg :携带的信息
 * @param key ：标明是哪个动作发起的数据加载
 */
sealed class NetWorkDataLoadState(val msg: String) {
    class Loading(msg: String = "",key: String = ""): NetWorkDataLoadState(msg)
    class Success(msg: String = "",key: String = ""): NetWorkDataLoadState(msg)
    class Fail(msg: String = "",key: String = ""): NetWorkDataLoadState(msg)
}