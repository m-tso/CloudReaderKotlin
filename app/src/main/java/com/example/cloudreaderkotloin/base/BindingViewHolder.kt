package com.example.cloudreaderkotloin.base
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView.ViewHolder

/**
 * @Description: 带binding的viewholder，可以使用该类代替所有的viewholder，通过binding来获取控件
 * @Author: tso 2020/8/31 14:24
 */
class BindingViewHolder<VDB: ViewDataBinding> : BaseViewHolder{
    lateinit var  binding: VDB
    constructor(_bining: VDB): super(_bining.root){
        binding = _bining
    }

    constructor(view: View): super(view){
        binding = DataBindingUtil.bind<VDB>(view)!!
    }
}