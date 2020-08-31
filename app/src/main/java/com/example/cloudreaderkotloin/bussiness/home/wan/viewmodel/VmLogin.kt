package com.example.cloudreaderkotloin.bussiness.home.wan.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cloudreaderkotloin.base.NetWorkViewModel
import com.example.cloudreaderkotloin.bussiness.common.bean.NetWorkDataLoadState
import com.example.cloudreaderkotloin.bussiness.common.utils.handelConnectExecption
import com.example.cloudreaderkotloin.bussiness.home.wan.bean.LoginData
import com.example.cloudreaderkotloin.bussiness.home.wan.model.retrofit.RetrofitManager
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class VmLogin : NetWorkViewModel() {
    val ldLoginResult = MutableLiveData<LoginData>()
    val wanApi = RetrofitManager.wanApiService

    fun login(username: String,password: String){
        viewModelScope.launch(handelConnectExecption(ldLoadState)){
            val loginResp = async { wanApi.login(username,password) }.await()
            if (loginResp.data != null){
                loginResp.data.password = password
                ldLoginResult.value = loginResp.data
                Log.i("login_success", loginResp.data.toString())
            }else{
                ldLoadState.value = NetWorkDataLoadState.Fail(loginResp.errorMsg)
            }


        }
    }



}