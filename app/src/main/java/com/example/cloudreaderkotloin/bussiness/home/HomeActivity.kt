package com.example.cloudreaderkotloin.bussiness.home


import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.cloudreaderkotloin.R
import com.example.cloudreaderkotloin.base.NetWorkActivity
import com.example.cloudreaderkotloin.bussiness.collection.CollectionActivity
import com.example.cloudreaderkotloin.bussiness.common.utils.*
import com.example.cloudreaderkotloin.bussiness.home.wan.activity.LoginActivity
import com.example.cloudreaderkotloin.bussiness.home.wan.bean.LoginEvent
import com.example.cloudreaderkotloin.bussiness.search.SearchActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class HomeActivity(override val contentViewId: Int = R.layout.activity_home) :
    NetWorkActivity<HomeViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flowStatusBarForDrawerlayout(resources.getColor(R.color.colorTheme), this, drawer)
        initView()
    }

    private fun initView() {
        navView.inflateHeaderView(R.layout.nav_main_header)
        val navHeaderView = navView.getHeaderView(0)
        val llNavCollection = navHeaderView.findViewById<LinearLayout>(R.id.ll_nav_collections)

        llNavCollection.setOnClickListener { dealDrawerClick(it.id)}




        nav_menu.setOnClickListener {
            drawer.openDrawer(Gravity.LEFT)
            if (isLogin()) {
                navView.getHeaderView(0).findViewById<TextView>(R.id.tv_nav_username)
                    .text = getString(USER_NAME)
            }
        }
        article_search.setOnClickListener { SearchActivity.start(this) }

    }

    private fun dealDrawerClick(viewId: Int) {
        drawer.closeDrawer(GravityCompat.START)
        val runnable = {
            when (viewId) {
                R.id.ll_nav_collections -> {
                    val intent = if (!isLogin()) {
                        Intent(this, LoginActivity::class.java)
                    } else {
                        Intent(this, CollectionActivity::class.java)
                    }
                    startActivity(intent)
                }


            }
        }

        drawer.postDelayed(runnable, 400)


    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onLoginSuccess(success: LoginEvent) {
        if (success.success) {
            startActivity(Intent(this, CollectionActivity::class.java))
        }

    }


}