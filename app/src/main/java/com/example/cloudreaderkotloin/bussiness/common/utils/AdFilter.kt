package com.example.cloudreaderkotloin.bussiness.common.utils

import android.content.Context
import android.content.res.AssetManager
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

/**
 * @Description: js文件加载器，允许部分注释符号
 * @Author: tso 2020/9/1 9:13
 */
fun Context.parseJs(path: String): String{
    val assetManager: AssetManager = assets
    val inputStream: InputStream = assetManager.open(path)
    val isr = InputStreamReader(inputStream, "UTF-8")
    val br = BufferedReader(isr)

    var length: String? = null
    val stringBuilder = StringBuilder()

    while (br.readLine().also { length = it } != null) {
        if (!length!!.startsWith("//",true )
            && !length!!.startsWith("/*",true)
            && !length!!.startsWith("*",true)){
            stringBuilder.append(length)
        }
    }
    val js = stringBuilder.toString()
    inputStream.close()
    return js

}