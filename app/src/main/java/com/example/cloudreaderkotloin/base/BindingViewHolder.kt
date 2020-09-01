package com.example.cloudreaderkotloin.base
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView.ViewHolder

/**
 * @Description: 带binding的viewholder，可以使用该类代替所有的viewholder，通过binding来获取控件
 * @Author: tso 2020/8/31 14:24
 */
class BindingViewHolder<VDB: ViewDataBinding>(val binding: VDB) : BaseViewHolder(binding.root)