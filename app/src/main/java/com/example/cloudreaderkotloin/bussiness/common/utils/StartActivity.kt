package com.example.cloudreaderkotloin.bussiness.common.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

/**
 * @Description: 启动Activity
 * @Author: tso 2020/9/1 16:36
 */

inline fun <reified T : AppCompatActivity> Context.startMyActivity(bundle: Bundle = Bundle()) {
    val intent = Intent(this, T::class.java)
    intent.putExtras(bundle)
    startActivity(intent)
}

inline fun <reified T : AppCompatActivity> Fragment.startMyActivity(bundle: Bundle = Bundle()) {
    val intent = Intent(this.activity, T::class.java)
    intent.putExtras(bundle)
    startActivity(intent)
}


