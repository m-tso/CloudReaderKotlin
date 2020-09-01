package com.example.cloudreaderkotloin.base
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlin.properties.Delegates

/**
 * @Description: 封装viewholder，使用标记位tag表明viewholder类型
 * @Author: tso 2020/8/31 14:20
 */
open class BaseViewHolder : ViewHolder {
    //用于标记viewholder类型
    var tag: Int = -1

    constructor(view: View): super(view)
}