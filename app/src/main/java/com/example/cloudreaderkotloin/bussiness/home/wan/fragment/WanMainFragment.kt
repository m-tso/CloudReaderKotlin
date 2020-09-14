package com.example.cloudreaderkotloin.bussiness.home.wan.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cloudreaderkotloin.R
import com.example.cloudreaderkotloin.base.NetWorkFragment
import com.example.cloudreaderkotloin.bussiness.common.utils.addArticle2Collections
import com.example.cloudreaderkotloin.bussiness.home.wan.adapter.PagingBannerArticleAdapter
import com.example.cloudreaderkotloin.bussiness.home.wan.adapter.PostsLoadStateAdapter
import com.example.cloudreaderkotloin.bussiness.home.wan.bean.LoginEvent
import com.example.cloudreaderkotloin.bussiness.home.wan.viewmodel.VmWanMain
import com.example.cloudreaderkotloin.databinding.WanMainBinding
import kotlinx.android.synthetic.main.fragment_wan_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class WanMainFragment(override val resId: Int = R.layout.fragment_wan_main) :
    NetWorkFragment<VmWanMain, WanMainBinding>() {



    private var rvWanAdapter: PagingBannerArticleAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }


    //初始化控件和数据
    private fun init() {
        //初始化recyclerview
        val layoutManager = LinearLayoutManager(this.context)
        rv_wan.layoutManager = layoutManager
        rvWanAdapter = PagingBannerArticleAdapter(this)
        rv_wan.adapter = rvWanAdapter?.withLoadStateFooter(PostsLoadStateAdapter(rvWanAdapter!!))

        viewModel?.pageData?.observe(viewLifecycleOwner, Observer {
            lifecycleScope.launchWhenResumed{
                rvWanAdapter?.submitData(it)
            }
        })

        //监听刷新状态当刷新完成之后关闭刷新
        lifecycleScope.launchWhenResumed {
            @OptIn(ExperimentalCoroutinesApi::class)
            rvWanAdapter?.loadStateFlow?.collectLatest {
                if(it.refresh is LoadState.NotLoading){

                    val runnable = {srl_wan?.isRefreshing = false}
                    rv_wan.postDelayed(runnable,1500)
                }
            }
        }

        //更新数据
        srl_wan.setOnRefreshListener {
            rvWanAdapter?.refresh()
        }

        srl_wan.isRefreshing = true
    }

    override fun onNetWorkConnect() {
        super.onNetWorkConnect()
        rvWanAdapter?.refresh()
    }



    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        rvWanAdapter?.refresh()
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