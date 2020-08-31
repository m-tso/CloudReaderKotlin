package com.example.cloudreaderkotloin.bussiness.home.wan.model.retrofit

import com.example.cloudreaderkotloin.bussiness.home.wan.bean.*
import com.example.cloudreaderkotloin.bussiness.home.wan.viewmodel.CollectState
import com.example.cloudreaderkotloin.bussiness.search.bean.WanHotSearchWordRequestData
import retrofit2.http.*

interface WanApiService {

    //获取玩安卓文章列表
    @GET("/article/list/{page}/json")
    suspend fun getArticles(@Path ("page") page: Int): WanArticleResp

    //获取玩安卓首页banner
    @GET("/banner/json")
    suspend fun getBanner(): WanBannerRequestData

    @GET("/hotkey/json")
    suspend fun getHotSearchWords(): WanHotSearchWordRequestData

    @POST("article/query/{page}/json")
    suspend fun getSearchArticles(@Path("page")page: Int,@Query("k")words: String): WanSearchResp

    @POST("/user/login")
    suspend fun login(@Query("username") username: String, @Query("password") password: String): LoginResp

    @POST("/lg/collect/{articleId}/json")
    suspend fun addToCollection(@Path("articleId") articleId: Int): CollectResp

    @POST("lg/uncollect_originId/{articleId}/json")
    suspend fun cancelCollect(@Path("articleId") articleId: Int): CollectResp

    @GET("lg/collect/list/{page}/json")
    suspend fun getCollectArticles(@Path("page") page: Int): CollectionsResp

    @POST("lg/uncollect/{articleId}/json")
    suspend fun cancelFromCollection(@Path("articleId") articleId: Int,@Query("originId") originId: Int): CollectResp

}