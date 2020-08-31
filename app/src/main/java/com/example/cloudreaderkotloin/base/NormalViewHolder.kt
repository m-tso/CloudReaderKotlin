package com.example.cloudreaderkotloin.base

import androidx.databinding.ViewDataBinding

//若无特别情况就使用该viewholder
open class NormalViewHolder : BaseViewHolder {
    constructor(binding: ViewDataBinding): super(binding.root){

    }
}