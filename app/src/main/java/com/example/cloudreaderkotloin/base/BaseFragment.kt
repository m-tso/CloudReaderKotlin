package com.example.cloudreaderkotloin.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType

open abstract class BaseFragment<VM: ViewModel,VDB: ViewDataBinding> : Fragment() {
    lateinit var binding: VDB
     var viewModel: VM ?= null
    abstract val resId: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(resId,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind<VDB>(view)!!
        initViewModel()
    }

    private fun initViewModel(){
        if (viewModel != null) return;
        var clzz: Class<VM>
        val type = this::class.java.genericSuperclass

        if (type is ParameterizedType) {
            clzz = type.actualTypeArguments[0] as Class<VM>
            viewModel = ViewModelProvider(this).get(clzz)
        } else {
            viewModel = ViewModelProvider(this).get(BaseViewModel::class.java) as VM
        }
    }

}