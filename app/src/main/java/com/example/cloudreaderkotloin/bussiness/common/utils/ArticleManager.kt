package com.example.cloudreaderkotloin.bussiness.common.utils

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cloudreaderkotloin.base.NetWorkViewModel
import com.example.cloudreaderkotloin.bussiness.home.wan.model.retrofit.RetrofitManager
import com.example.cloudreaderkotloin.bussiness.home.wan.viewmodel.CollectState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val wanMainApi = RetrofitManager.wanApiService

/**
 * @Description: 添加文章到收藏夹
 * @Author: tso 2020/8/27 18:25
 */

fun addArticle2Collections(
    articleId: Int,
    viewModel: NetWorkViewModel,
    add2Collect: MutableLiveData<CollectState>
) {
    viewModel.viewModelScope.launch(handelConnectExecption(viewModel.ldLoadState)) {
        val resp = withContext(Dispatchers.Default) {
            wanMainApi.addToCollection(articleId)
        }
        val collectState = CollectState()
        collectState.isSuccess = TextUtils.isEmpty(resp.errorMsg) && resp.errorCode == 0
        collectState.msg = resp.errorMsg
        collectState.articleId = articleId
        add2Collect.value = collectState
    }

}

/**
 * @Description: 将文章从收藏夹中删除
 * @Author: tso 2020/8/27 18:25
 */
fun cancleCollect(
    articleId: Int,
    viewModel: NetWorkViewModel,
    cancleCollect: MutableLiveData<CollectState>
) {
    viewModel.viewModelScope.launch(handelConnectExecption(viewModel.ldLoadState)) {
        val resp = withContext(Dispatchers.Default) {
            wanMainApi.cancelCollect(articleId)
        }
        val collectState = CollectState()
        collectState.isSuccess = TextUtils.isEmpty(resp.errorMsg) && resp.errorCode == 0
        collectState.msg = resp.errorMsg
        collectState.articleId = articleId
        cancleCollect.value = collectState

        Log.i("resppp",resp.errorMsg+ "hhhhhhh")

    }
}

