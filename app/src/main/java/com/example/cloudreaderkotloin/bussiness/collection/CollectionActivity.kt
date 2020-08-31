package com.example.cloudreaderkotloin.bussiness.collection

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.example.cloudreaderkotloin.R
import com.example.cloudreaderkotloin.base.NetWorkActivity
import com.example.cloudreaderkotloin.base.XRecyclerView
import com.example.cloudreaderkotloin.bussiness.collection.adapter.CollectArticleAdapter
import com.example.cloudreaderkotloin.bussiness.collection.viewmodel.LoadType
import com.example.cloudreaderkotloin.bussiness.collection.viewmodel.VmCollections
import com.example.cloudreaderkotloin.bussiness.common.bean.NetWorkDataLoadState
import com.example.cloudreaderkotloin.bussiness.common.utils.WrapContentLinearLayoutManager
import com.example.cloudreaderkotloin.bussiness.common.utils.flowStatusBar
import kotlinx.android.synthetic.main.activity_collection.*

class CollectionActivity(override val contentViewId: Int = R.layout.activity_collection ) : NetWorkActivity<VmCollections>() {

    val showSrlTag = "show"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flowStatusBar(getColor(R.color.colorTheme),collection_status_bar_background)
        initView()
        initObserveData()

    }

    private fun initObserveData(){
        loadState?.observe(this, Observer {
            when(loadState?.value){
                is NetWorkDataLoadState.Success ->{ srl_collect.isRefreshing = false }

                is NetWorkDataLoadState.Loading ->  {
                    if (showSrlTag == loadState!!.value!!.msg) {srl_collect.isRefreshing = true }
                }

                is NetWorkDataLoadState.Fail -> { srl_collect.isRefreshing = false }
            }
        })
    }

    private fun initView(){
        img_collection_back.setOnClickListener { finish() }
        rv_collection.layoutManager = WrapContentLinearLayoutManager(this)
        rv_collection.setXRvAdapter(CollectArticleAdapter(this,viewModel!!))
        rv_collection.openLoadMoreView()
        rv_collection.loadMoreListener = object : XRecyclerView.LoadMoreListener{
            override fun onStartLoadMore(loadMoreView: View) {
                viewModel?.getCollectionByPage(LoadType.LoadMore())
            }

            override fun onLoadMoreFinish(loadMoreView: View) {

            }

        }

        viewModel?.getCollectionByPage(LoadType.LoadFirstPage(showSrlTag))

        //更新数据
        srl_collect.setOnRefreshListener {
            viewModel?.getCollectionByPage(LoadType.LoadRefresh(showSrlTag))
        }



    }

}