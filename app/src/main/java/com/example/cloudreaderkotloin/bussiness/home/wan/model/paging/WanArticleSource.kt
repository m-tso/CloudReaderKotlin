package com.example.cloudreaderkotloin.bussiness.home.wan.model.paging

import androidx.paging.PagingSource
import com.example.cloudreaderkotloin.bussiness.home.wan.model.retrofit.RetrofitManager


import java.lang.Exception

class WanArticleSource : PagingSource<Int, Any>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Any> {
        val page = params.key ?: 0
        return try {

            //把banner和文章的第一页当做recyclerview的第一页，方便刷新操作
            if (page == 0){
                val bannerResp = RetrofitManager.wanApiService.getBanner()
                val firstPageArticle = RetrofitManager.wanApiService.getArticles(0)
                val firstPageData = ArrayList<Any>()
                firstPageData.add(bannerResp)
                firstPageData.addAll(firstPageArticle.data.datas)

                return LoadResult.Page(
                    data = firstPageData,
                    prevKey = null,
                    nextKey =  page + 1
                )

            }else{
                val resp = RetrofitManager.wanApiService.getArticles(page)
                return LoadResult.Page(
                    data = resp.data.datas,
                    prevKey = null,
                    nextKey = if (page == resp.data.pageCount) null else page + 1

                )
            }

        }catch (e: Exception){
            LoadResult.Error(e)
        }
    }
}