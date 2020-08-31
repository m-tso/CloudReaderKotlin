package com.example.cloudreaderkotloin.base
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlin.properties.Delegates

open class BaseViewHolder : ViewHolder {
    //用于标记viewholder类型
    var tag: Int = -1
    constructor(view: View): super(view)
}