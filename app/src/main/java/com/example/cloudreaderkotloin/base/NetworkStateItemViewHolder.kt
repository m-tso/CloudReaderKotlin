package com.example.cloudreaderkotloin.base

import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.paging.LoadState
import androidx.paging.LoadState.Error
import androidx.recyclerview.widget.RecyclerView
import com.example.cloudreaderkotloin.R


class NetworkStateItemViewHolder(
    parent: ViewGroup,
    private val retryCallback: () -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.view_load_more, parent, false)
) {
    val imageView = itemView.findViewById<ImageView>(R.id.loadmore_animation)
    val textView =itemView.findViewById<TextView>(R.id.tv_no_more_tag)
    val loadMoreAnimation = imageView.drawable as AnimationDrawable
    val context = parent.context

    fun bindTo(loadState: LoadState,itemCount: Int) {

        imageView.visibility = View.VISIBLE
        loadMoreAnimation.start()
        when(loadState){
             is LoadState.Loading -> {
                if (itemCount <=1){
                    imageView.visibility = View.GONE

                    textView.visibility = View.GONE
                }
            }

           LoadState.NotLoading(true) -> {
                imageView.visibility = View.GONE
                textView.visibility = View.VISIBLE
                loadMoreAnimation.stop()
            }

            is Error -> {
                Toast.makeText(context,"网络异常",Toast.LENGTH_SHORT).show()
                imageView.visibility = View.GONE
            }


        }


    }
}
