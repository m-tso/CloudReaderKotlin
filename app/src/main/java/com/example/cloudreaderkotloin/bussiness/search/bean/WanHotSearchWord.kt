package com.example.cloudreaderkotloin.bussiness.search.bean

data class WanHotSearchWordRequestData(
    val `data`: List<WanHotSearchWord>,
    val errorCode: Int,
    val errorMsg: String
)

data class WanHotSearchWord(
    val id: Int,
    val link: String,
    val name: String,
    val order: Int,
    val visible: Int
)