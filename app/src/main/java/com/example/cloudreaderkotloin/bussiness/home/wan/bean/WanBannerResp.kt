package com.example.cloudreaderkotloin.bussiness.home.wan.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WanBannerRequestData(
    val `data`: List<WanBanner>,
    val errorCode: Int,
    val errorMsg: String
): Parcelable

@Parcelize
data class WanBanner(
    val desc: String,
    val id: Int,
    val imagePath: String,
    val isVisible: Int,
    val order: Int,
    val title: String,
    val type: Int,
    val url: String
):Parcelable