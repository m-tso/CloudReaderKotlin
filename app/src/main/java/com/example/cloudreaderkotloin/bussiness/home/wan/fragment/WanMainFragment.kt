package com.example.cloudreaderkotloin.bussiness.home.wan.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cloudreaderkotloin.R
import com.example.cloudreaderkotloin.base.NetWorkFragment
import com.example.cloudreaderkotloin.base.XRecyclerView.LoadMoreListener
import com.example.cloudreaderkotloin.bussiness.common.bean.NetWorkDataLoadState
import com.example.cloudreaderkotloin.bussiness.common.utils.addArticle2Collections
import com.example.cloudreaderkotloin.bussiness.common.utils.checkConnectIsAvailable
import com.example.cloudreaderkotloin.bussiness.home.wan.adapter.WanBannerArticleAdapter
import com.example.cloudreaderkotloin.bussiness.home.wan.bean.LoginEvent
import com.example.cloudreaderkotloin.bussiness.home.wan.viewmodel.VmWanMain
import com.example.cloudreaderkotloin.bussiness.home.wan.viewmodel.WanMainLiveData
import com.example.cloudreaderkotloin.databinding.WanMainBinding
import kotlinx.android.synthetic.main.fragment_wan_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class WanMainFragment(override val resId: Int = R.layout.fragment_wan_main) :
    NetWorkFragment<VmWanMain, WanMainBinding>() {
    private var loadState: MutableLiveData<NetWorkDataLoadState>? = null
    private var rvWanAdapter: WanBannerArticleAdapter? = null
    private var wanMainData: MutableLiveData<WanMainLiveData>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()


    }

    override fun onResume() {
        super.onResume()
        rv_wan.mViewData
    }

    //初始化控件和数据
    private fun init() {
        //初始化recyclerview
        val layoutManager = LinearLayoutManager(this.context)
        rv_wan.layoutManager = layoutManager
        rvWanAdapter = WanBannerArticleAdapter(this)
        rv_wan.setXRvAdapter(rvWanAdapter!!)
        rv_wan.openLoadMoreView()

        rv_wan.loadMoreListener = object : LoadMoreListener {
            override fun onStartLoadMore(v: View) {
                v.visibility = if (checkConnectIsAvailable()) View.VISIBLE else View.GONE

                if (checkConnectIsAvailable() && wanMainData?.value != null){
                    rvWanAdapter?.loadNewPageArticles()
                }
            }

            override fun onLoadMoreFinish(loadMoreView: View) {

            }
        }


        //更新数据
        srl_wan.setOnRefreshListener {
            viewModel?.getWanFirstPageData()
        }

        //进度监听
        loadState = viewModel?.ldLoadState
        wanMainData = viewModel?.wanMainData

        loadState?.observe(viewLifecycleOwner, Observer {
            when (loadState?.value) {
                is NetWorkDataLoadState.Fail -> {
                    srl_wan.isRefreshing = false
                    Toast.makeText(activity, loadState?.value?.msg, Toast.LENGTH_SHORT).show()
                    if (wanMainData!!.value == null){
                        lltips.visibility = View.VISIBLE
                    }
                }

                is NetWorkDataLoadState.Loading -> srl_wan.isRefreshing = true

                is NetWorkDataLoadState.Success -> {
                    srl_wan.isRefreshing = false
                    lltips.visibility = View.GONE

                }
            }
        })
    }

    override fun onNetWorkConnect() {
        super.onNetWorkConnect()
        viewModel?.getWanFirstPageData()
    }



    override fun onDetach() {
        super.onDetach()
        rvWanAdapter?.getBannerView()?.stopAutoPlay()
        EventBus.getDefault().unregister(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        rvWanAdapter?.getBannerView()?.startAutoPlay()
        viewModel?.getWanFirstPageData()

        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    fun onLoginSuccess(success: LoginEvent){
        if(success.success){
            if (success.articleId != -1){
                addArticle2Collections(success.articleId,viewModel!!,viewModel!!.add2Collect)
            }

            viewModel?.getWanFirstPageData()
        }

    }



}