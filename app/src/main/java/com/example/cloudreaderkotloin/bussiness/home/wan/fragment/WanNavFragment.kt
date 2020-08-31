package com.example.cloudreaderkotloin.bussiness.home.wan.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cloudreaderkotloin.R
import com.example.cloudreaderkotloin.base.BaseFragment

class WanNavFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wan_nav,container,false)
    }
}