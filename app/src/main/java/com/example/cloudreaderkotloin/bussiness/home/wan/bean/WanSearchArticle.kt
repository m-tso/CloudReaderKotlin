package com.example.cloudreaderkotloin.bussiness.home.wan.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WanSearchResp(
    val `data`: ArticleData,
    val errorCode: Int,
    val errorMsg: String
): Parcelable


