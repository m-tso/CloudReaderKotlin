package com.example.cloudreaderkotloin.bussiness.home.wan.bean

data class LoginResp(
    val `data`: LoginData,
    val errorCode: Int,
    val errorMsg: String
)

data class LoginData(
    val admin: Boolean,
    val chapterTops: List<Any>,
    val coinCount: Int,
    val collectIds: List<Any>,
    val email: String,
    val icon: String,
    val id: Int,
    val nickname: String,
    var password: String,
    val publicName: String,
    val token: String,
    val type: Int,
    val username: String
)