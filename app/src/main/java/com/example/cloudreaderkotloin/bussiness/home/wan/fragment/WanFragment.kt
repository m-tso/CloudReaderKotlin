package com.example.cloudreaderkotloin.bussiness.home.wan.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cloudreaderkotloin.R
import com.example.cloudreaderkotloin.base.BaseFragment
import com.example.cloudreaderkotloin.bussiness.home.adapter.WanFragmentPagerAdapter

import com.example.cloudreaderkotloin.bussiness.home.wan.viewmodel.VmWan
import com.example.cloudreaderkotloin.databinding.WanBinding
import kotlinx.android.synthetic.main.fragment_wan.*

class WanFragment(override val resId: Int = R.layout.fragment_wan) : BaseFragment<VmWan,WanBinding>() {
    val mTitles = listOf<String>("玩安卓","发现","体系","导航")
    val mFragments = ArrayList<Fragment>()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        mFragments.add(WanMainFragment())
        mFragments.add(WanFindFragment())
        mFragments.add(WanClassifyFragment())
        mFragments.add(WanNavFragment())

        wan_content.adapter = WanFragmentPagerAdapter(childFragmentManager, mFragments, mTitles)
        wan_content.offscreenPageLimit = 3
        wan_title.setupWithViewPager(wan_content)


    }


}