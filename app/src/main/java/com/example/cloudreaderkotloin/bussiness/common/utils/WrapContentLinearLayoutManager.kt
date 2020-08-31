package com.example.cloudreaderkotloin.bussiness.common.utils

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.IndexOutOfBoundsException

class WrapContentLinearLayoutManager : LinearLayoutManager {

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attributeSet: AttributeSet, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attributeSet, defStyleAttr, defStyleRes)

    constructor(context: Context?, orientation: Int, reverseLayout: Boolean)
            : super(context, orientation, reverseLayout)

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        }catch (e: IndexOutOfBoundsException){
            e.printStackTrace()
        }

    }
}