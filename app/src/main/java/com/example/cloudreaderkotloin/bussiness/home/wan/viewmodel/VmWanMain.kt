package com.example.cloudreaderkotloin.bussiness.home.wan.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.cloudreaderkotloin.base.NetWorkViewModel
import com.example.cloudreaderkotloin.bussiness.common.bean.NetWorkDataLoadState
import com.example.cloudreaderkotloin.bussiness.common.utils.handelConnectExecption
import com.example.cloudreaderkotloin.bussiness.home.wan.bean.Article
import com.example.cloudreaderkotloin.bussiness.home.wan.bean.WanBanner
import com.example.cloudreaderkotloin.bussiness.home.wan.model.paging.WanArticleSource
import com.example.cloudreaderkotloin.bussiness.home.wan.model.retrofit.RetrofitManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull


class VmWanMain : NetWorkViewModel() {

    val wanMainData = MutableLiveData<WanMainLiveData>()
    val specifyPageArticle = MutableLiveData<List<Article>>()
    val cancleCollect = MutableLiveData<CollectState>()
    val add2Collect = MutableLiveData<CollectState>()

    private val TAG = "VmWanMain"
    private val wanMainApi = RetrofitManager.wanApiService

    //玩安卓的页数据，每次返回的value都是一页的数据
    val pageData =
        Pager(PagingConfig(pageSize = 100,initialLoadSize = 500,prefetchDistance = 500)){
        WanArticleSource()
    }.flow.asLiveData()




    /**
     * @Description: 提供玩安卓首页的banner和article数据，在两个数据均加载完成后再返回
     * @Author: tso 2020/8/6 11:33
     */
    fun getWanFirstPageData(): MutableLiveData<WanMainLiveData> {


        viewModelScope.launch(handelConnectExecption(ldLoadState)) {

            ldLoadState.value = NetWorkDataLoadState.Loading()
            val bannerRequestData = withTimeoutOrNull(5000L) { wanMainApi.getBanner() }
            val articleRequestData = withTimeoutOrNull(5000L) { wanMainApi.getArticles(0) }


            if (articleRequestData == null || bannerRequestData == null) {
                ldLoadState.value = NetWorkDataLoadState.Fail("连接超时")

            } else {
                val newData = WanMainLiveData()
                newData.lsBannerAndArticles.clear()
                newData.lsBannerAndArticles.add(bannerRequestData.data)
                newData.lsBannerAndArticles.addAll(articleRequestData.data.datas)

                if (wanMainData.value == null || checkArticleIsNotUpdate(newData)) {
                    wanMainData.value = newData
                }
                ldLoadState.value = NetWorkDataLoadState.Success()
            }
        }

        return wanMainData
    }


    /**
     * @Description: 获取指定页面的文章
     * @Author: tso 2020/8/7 15:05
     * @param page 页
     */
    fun loadArticleByPage(page: Int) {
        viewModelScope.launch(handelConnectExecption(ldLoadState)) {
            val articleRequestData = wanMainApi.getArticles(page)
            if (articleRequestData.data.datas != null) {
                //延迟1s，让加载动画能够给用户看到
                delay(1000)
                specifyPageArticle.value = articleRequestData.data.datas
            }
        }
    }

    /**
     * @Description: 检查首页是否有更新，当前该方法存在问题：仅仅检查文章是否有更新
     * @Author: tso 2020/8/18 10:59
     */
    private fun checkArticleIsNotUpdate(wanMainLiveData: WanMainLiveData): Boolean {
        val firstArticle = wanMainData.value!!.lsBannerAndArticles[1] as Article
        val article = wanMainLiveData.lsBannerAndArticles[1] as Article
        val bannerList = wanMainData.value!!.lsBannerAndArticles[0] as List<WanBanner>

        if (!bannerList.containsAll(wanMainLiveData.lsBannerAndArticles[0] as List<WanBanner>)) {
            return true
        }

        if (firstArticle.title != article.title) { return true }

        val size = wanMainData!!.value!!.lsBannerAndArticles.size

        for (i in 0 until size){
            if (wanMainLiveData.lsBannerAndArticles[i] != wanMainData!!.value!!.lsBannerAndArticles[i]){
                wanMainLiveData.updateAll = false
                return true
            }
        }

        return false
    }

}


/**
 * @Description: VmWanMain的对外数据，封装banner和article数据
 * @Author: tso 2020/8/6 11:40
 */
class WanMainLiveData {
    var lsBannerAndArticles = ArrayList<Any>()
    var updateAll = true
}

class CollectState{
    var isSuccess: Boolean = false
    var msg: String? = null
    var articleId: Int = -1
}


