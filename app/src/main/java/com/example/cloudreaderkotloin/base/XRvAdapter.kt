package com.example.cloudreaderkotloin.base


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.databinding.ObservableList.OnListChangedCallback
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView


abstract class XRvAdapter<T> :
    RecyclerView.Adapter<BindingViewHolder<*>>() {
    lateinit var mRecyclerView: XRecyclerView
    lateinit var mAdapter: RecyclerView.Adapter<*>
    override fun getItemCount(): Int {
        return 0
    }


}
