package com.example.cloudreaderkotloin.bussiness.common.utils

import android.R
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import com.example.cloudreaderkotloin.bussiness.common.ui.StatusBarView


fun calculateStatusColor(@ColorInt color: Int, alpha: Int): Int {
    val a = 1 - alpha / 255f
    var red = color shr 16 and 0xff
    var green = color shr 8 and 0xff
    var blue = color and 0xff
    red = (red * a + 0.5).toInt()
    green = (green * a + 0.5).toInt()
    blue = (blue * a + 0.5).toInt()
    return 0xff shl 24 or (red shl 16) or (green shl 8) or blue
}

/**
 * 生成一个和状态栏大小相同的彩色矩形条
 *
 * @param activity 需要设置的 activity
 * @param color    状态栏颜色值
 * @return 状态栏矩形条
 */
fun createStatusBarView(activity: Activity, @ColorInt color: Int): StatusBarView? {
    // 绘制一个和状态栏一样高的矩形
    val statusBarView = StatusBarView(activity)
    val params = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        getStatusBarHeight(activity)
    )
    statusBarView.layoutParams = params
    statusBarView.setBackgroundColor(color)
    return statusBarView
}

/**
 * 添加半透明矩形条
 *
 * @param activity       需要设置的 activity
 * @param statusBarAlpha 透明值
 */
 fun addTranslucentView(activity: Activity, statusBarAlpha: Int) {
    val contentView =
        activity.findViewById<View>(R.id.content) as ViewGroup
    if (contentView.childCount > 1) {
        contentView.getChildAt(1)
            .setBackgroundColor(Color.argb(statusBarAlpha, 0, 0, 0))
    } else {
        contentView.addView(createTranslucentStatusBarView(activity, statusBarAlpha))
    }
}

/**
 * 创建半透明矩形 View
 *
 * @param alpha 透明值
 * @return 半透明 View
 */
 fun createTranslucentStatusBarView(activity: Activity, alpha: Int): StatusBarView? {
    // 绘制一个和状态栏一样高的矩形
    val statusBarView = StatusBarView(activity)
    val params = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        getStatusBarHeight(activity)
    )
    statusBarView.layoutParams = params
    statusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0))
    return statusBarView
}


fun setViewHighAsStatusBar(view: View){
    val params = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        getStatusBarHeight(view.context)
    )
    view.layoutParams = params
}

/**
 * @Description: 获取状态栏高度
 * @Author: tso 2020/7/21 15:47
 * @return 返回高度
 */

fun getStatusBarHeight(context : Context):Int{
    var resourceId = context.resources.getIdentifier("status_bar_height","dimen","android")
    return if (resourceId > 0) context.resources.getDimensionPixelOffset(resourceId) else 0
}