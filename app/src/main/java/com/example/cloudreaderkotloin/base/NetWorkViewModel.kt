package com.example.cloudreaderkotloin.base

import androidx.lifecycle.MutableLiveData
import com.example.cloudreaderkotloin.bussiness.common.bean.NetWorkDataLoadState

open class NetWorkViewModel : BaseViewModel() {
    val ldLoadState = MutableLiveData<NetWorkDataLoadState>()
}