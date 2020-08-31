package com.example.cloudreaderkotloin.base
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView.ViewHolder
class BindingViewHolder<VDB: ViewDataBinding>(val binding: VDB) : BaseViewHolder(binding.root)