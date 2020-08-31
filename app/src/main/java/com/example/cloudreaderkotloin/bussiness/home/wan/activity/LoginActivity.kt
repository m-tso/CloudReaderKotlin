package com.example.cloudreaderkotloin.bussiness.home.wan.activity

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.cloudreaderkotloin.R
import com.example.cloudreaderkotloin.base.NetWorkActivity
import com.example.cloudreaderkotloin.bussiness.common.utils.*
import com.example.cloudreaderkotloin.bussiness.home.wan.bean.LoginEvent
import com.example.cloudreaderkotloin.bussiness.home.wan.viewmodel.VmLogin
import kotlinx.android.synthetic.main.activity_login.*
import org.greenrobot.eventbus.EventBus

class LoginActivity(override val contentViewId: Int = R.layout.activity_login) :
    NetWorkActivity<VmLogin>() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flowStatusBar(resources.getColor(R.color.colorTheme), login_status_bar_background)
        setSupportActionBar(login_toolbar)



        initView()
        initObserveData()
    }

    private fun initView() {
        img_login_back.setOnClickListener { finish() }
        bt_login.setOnClickListener {
            val userName = edt_username.text.toString().trim()
            val password = edt_password.text.toString().trim()

            if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
                viewModel?.login(userName, password)
                Toast.makeText(this, "begin login", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "账号和密码均不能为空", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun initObserveData(){
        //登录成功后 1.关闭当前Activity 2.同步articlelist中的图标 3.将用户信息保存
        viewModel?.ldLoginResult?.observe(this, Observer {
            saveString(USER_NAME,it.nickname)
            saveString(PASSWORD,it.password)
            saveArray(ARTICLE_COLLECTION,it.collectIds as List<String>)
            Log.i("collections", it.collectIds.toString())
            val event = LoginEvent(true)
            event.articleId = intent.getIntExtra("articleId",-1)
            EventBus.getDefault().postSticky(event)
            finish()

            Log.i("collections", getArray<String>(ARTICLE_COLLECTION).toString())
        })
    }


}