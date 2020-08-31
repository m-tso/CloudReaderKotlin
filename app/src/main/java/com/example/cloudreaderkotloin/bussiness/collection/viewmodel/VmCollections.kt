package com.example.cloudreaderkotloin.bussiness.collection.viewmodel

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cloudreaderkotloin.base.NetWorkViewModel
import com.example.cloudreaderkotloin.bussiness.common.bean.NetWorkDataLoadState
import com.example.cloudreaderkotloin.bussiness.common.utils.handelConnectExecption
import com.example.cloudreaderkotloin.bussiness.common.utils.wanMainApi
import com.example.cloudreaderkotloin.bussiness.home.wan.bean.CollectArticle
import com.example.cloudreaderkotloin.bussiness.home.wan.model.retrofit.RetrofitManager
import com.example.cloudreaderkotloin.bussiness.home.wan.viewmodel.CollectState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VmCollections : NetWorkViewModel() {
    val collectionsData = MutableLiveData<CollectionLoadData>()
    val wanApi = RetrofitManager.wanApiService
    val cancleCollect = MutableLiveData<CollectState>()

    var toPage = 1

    fun getCollectionByPage(loadType: LoadType) {
        var page = if (loadType is LoadType.LoadMore) toPage else 0

        viewModelScope.launch(handelConnectExecption(ldLoadState)) {
            ldLoadState.value = NetWorkDataLoadState.Loading(loadType.msg)
            val collectionsResp = async { wanApi.getCollectArticles(page) }.await()
            val collectArticles = collectionsResp.data.datas

            if (collectArticles != null) {
                val loadData = CollectionLoadData()
                loadData.data = collectArticles
                loadData.loadType = loadType
                collectionsData.value = loadData
                ldLoadState.value = NetWorkDataLoadState.Success("")
                toPage = if (loadType is LoadType.LoadMore) toPage+1 else toPage

            } else {
                ldLoadState.value = NetWorkDataLoadState.Fail(collectionsResp.errorMsg)
            }
        }
    }

    fun cancleFromCollection(articleId: Int,originId: Int) {
        viewModelScope.launch(handelConnectExecption(ldLoadState)) {
            val resp = withContext(Dispatchers.Default) {
                wanMainApi.cancelFromCollection(articleId,originId)
            }
            val collectState = CollectState()
            collectState.isSuccess = TextUtils.isEmpty(resp.errorMsg) && resp.errorCode == 0
            collectState.msg = resp.errorMsg
            collectState.articleId = articleId
            cancleCollect.value = collectState

        }
    }
}

class CollectionLoadData {
    var loadType: LoadType? = null
    var data: List<CollectArticle>? = null
}

sealed class LoadType(val msg: String = "") {
    class LoadMore(val sMsg: String = "") : LoadType(sMsg)
    class LoadFirstPage(val sMsg: String = "") : LoadType(sMsg)
    class LoadRefresh(val sMsg: String = ""): LoadType(sMsg)

}