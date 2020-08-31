package com.example.cloudreaderkotloin.bussiness.home.wan.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WanArticleResp(
    val data: ArticleData,
    val errorCode: Int,
    val errorMsg: String
):Parcelable

@Parcelize
data class ArticleData(
    val curPage: Int,
    val datas: List<Article>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
):Parcelable

@Parcelize
data class Article(
    val apkLink: String,
    val audit: Int,
    val author: String,
    val canEdit: Boolean,
    val chapterId: Int,
    val chapterName: String,
    var collect: Boolean,
    val courseId: Int,
    val desc: String,
    val descMd: String,
    val envelopePic: String,
    val fresh: Boolean,
    val id: Int,
    val link: String,
    val niceDate: String,
    val niceShareDate: String,
    val origin: String,
    val prefix: String,
    val projectLink: String,
    val publishTime: Long,
    val realSuperChapterId: Int,
    val selfVisible: Int,
    val shareDate: Long,
    val shareUser: String,
    val superChapterId: Int,
    val superChapterName: String,
    val tags: List<Tag>,
    val title: String,
    val type: Int,
    val userId: Int,
    val visible: Int,
    val zan: Int

):Parcelable

@Parcelize
data class Tag(
    val name: String,
    val url: String
):Parcelable
