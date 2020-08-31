package com.example.cloudreaderkotloin.bussiness.common.utils
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.cloudreaderkotloin.bussiness.common.ui.StatusBarView

/**
 * @Description: 通过使用view设置statusbar的底色达到“假”的沉浸效果：
 *              实现方式：设置view的高度为statusbar的高度、设置view的底色自定义的底色，然后使用
 *              view顶在statusbar底部
 *              <p> 该方法需要配合drawelayout在xml下设置fitsystemwindow = true 使用，具体原因尚未明白
 *              注释待修改
 * @Author:     tso 2020/7/22 17:22
 */
fun flowStatusBarForDrawerlayout(color: Int, activity: AppCompatActivity, view:ViewGroup) {
    val contentLayout = view.getChildAt(0) as ViewGroup

    if (contentLayout.childCount > 0 && contentLayout.getChildAt(0) is StatusBarView) {
        contentLayout.getChildAt(0).setBackgroundColor(calculateStatusColor(color, 0))
    } else {
        val statusBarView = createStatusBarView(activity, color)
        contentLayout.addView(statusBarView, 0)
    }
    view.fitsSystemWindows = false
}

fun flowStatusBar(color: Int,view: View){
    val params = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        getStatusBarHeight(view.context)
    )
    view.layoutParams = params
    view.setBackgroundColor(color)
}
