package com.example.cloudreaderkotloin.bussiness.home.wan.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CollectionsResp(
    val `data`: Data,
    val errorCode: Int,
    val errorMsg: String
): Parcelable

@Parcelize
data class Data(
    val curPage: Int,
    val datas: List<CollectArticle>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
): Parcelable

@Parcelize
data class CollectArticle(
    val author: String,
    val chapterId: Int,
    val chapterName: String,
    val courseId: Int,
    val desc: String,
    val envelopePic: String,
    val id: Int,
    val link: String,
    val niceDate: String,
    val origin: String,
    val originId: Int,
    val publishTime: Long,
    val title: String,
    val userId: Int,
    val visible: Int,
    val zan: Int
): Parcelable