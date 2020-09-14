package com.example.cloudreaderkotloin.bussiness.search.viewmodel

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cloudreaderkotloin.base.NetWorkViewModel
import com.example.cloudreaderkotloin.bussiness.common.bean.NetWorkDataLoadState
import com.example.cloudreaderkotloin.bussiness.common.utils.SEARCH_HISTORY
import com.example.cloudreaderkotloin.bussiness.common.utils.getString
import com.example.cloudreaderkotloin.bussiness.common.utils.handelConnectExecption
import com.example.cloudreaderkotloin.bussiness.common.utils.saveString
import com.example.cloudreaderkotloin.bussiness.home.wan.bean.Article
import com.example.cloudreaderkotloin.bussiness.home.wan.model.retrofit.RetrofitManager
import com.example.cloudreaderkotloin.bussiness.home.wan.viewmodel.CollectState
import com.example.cloudreaderkotloin.bussiness.search.bean.WanHotSearchWord
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class VmSearch : NetWorkViewModel() {
    val hotSearchWorld = MutableLiveData<List<WanHotSearchWord>>()
    val articlesWithWords = MutableLiveData<List<Article>>()
    val wanSearchLiveData = MutableLiveData<WanSearchData>()
    var searchWord: String = ""
    var gson = Gson()
    var historys: ArrayList<String>? = null
    val ldHistory = MutableLiveData<ArrayList<String>>()

    val cancleCollect = MutableLiveData<CollectState>()
    val add2Collect = MutableLiveData<CollectState>()


    //用于标记网络数据加载情况
    val loadState = MutableLiveData<NetWorkDataLoadState>()
    val wanApiService = RetrofitManager.wanApiService

    fun refreshHotSearchWords() {

        viewModelScope.launch(handelConnectExecption(loadState)) {
            val wanHotSearchWordRequestData = wanApiService.getHotSearchWords()
            val wanHotSearchWord = wanHotSearchWordRequestData.data

            if (wanHotSearchWord != null) {
                if (hotSearchWorld.value == null
                    || !hotSearchWorld.value!!.containsAll(wanHotSearchWord)) {
                    hotSearchWorld.value = wanHotSearchWord
                }
            } else {
                loadState.value = NetWorkDataLoadState.Fail(wanHotSearchWordRequestData.errorMsg)
            }
        }
    }

    fun getWanArticleByWords(page: Int, words: String, searchType: SearchType) {
        if (searchType is SearchType.SearchNewWord) {
            saveHistory(words)
        }

        searchWord = words
        viewModelScope.launch(handelConnectExecption(loadState)) {
            val wanSearchRequestData =
                wanApiService.getSearchArticles(page, words)

            val articles = wanSearchRequestData.data.datas
            val wanSearchData = WanSearchData()
            wanSearchData.searchType = searchType

            if (articles != null) {
                wanSearchData.data = articles
                wanSearchLiveData.value = wanSearchData
            } else {
                loadState.value = NetWorkDataLoadState.Fail(wanSearchRequestData.errorMsg)
            }
        }
    }

    private fun saveHistory(keyword: String){
        if (!TextUtils.isEmpty(keyword)) {
            if (historys == null) {
                historys = getHistory()
            }
            if (historys != null) {
                if (historys!!.isNotEmpty()) {
                    historys!!.remove(keyword)
                }
                historys!!.add(0, keyword)
                if (historys!!.size > 12) {
                    historys!!.removeAt(historys!!.size - 1)
                }
            }
            if (gson == null) {
                gson = Gson()
            }
            saveString(
                SEARCH_HISTORY,
                gson.toJson(historys)
            )

        }
    }

    fun refreshHistory(){
        ldHistory.value = getHistory()
    }

    fun clearSearchHistory(){
        saveString(SEARCH_HISTORY,"")
        refreshHistory()
    }

    fun getHistory(): ArrayList<String> {
        return try {
            val details: String = getString(SEARCH_HISTORY)!!
            if (!TextUtils.isEmpty(details)) {
                if (gson == null) {
                    gson = Gson()
                }
                gson.fromJson<ArrayList<String>>(
                    details,
                    object : TypeToken<ArrayList<String?>?>() {}.type
                )
            } else {
                ArrayList()
            }
        } catch (e: Exception) {
            ArrayList()
        }
    }

}


class WanSearchData {
    var searchType: SearchType? = null
    var data: List<Article>? = null
}

sealed class SearchType() {
    class SearchNewWord() : SearchType()
    class SearchWordAppend() : SearchType()
    class SearchSameWord() : SearchType()
}
