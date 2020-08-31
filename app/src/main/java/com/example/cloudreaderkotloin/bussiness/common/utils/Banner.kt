package com.example.cloudreaderkotloin.bussiness.common.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.stx.xhb.xbanner.transformers.BasePageTransformer
import com.youth.banner.loader.ImageLoader

class GlideImageLoader : ImageLoader() {
    override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
        if (context != null && imageView != null) {
            Glide.with(context).load(path as String).into(imageView)
        }
    }
}

class NormalPageTransformer : BasePageTransformer() {
    override fun handleRightPage(view: View?, position: Float) {
        view!!.scaleY = 1f
    }

    override fun handleInvisiblePage(view: View?, position: Float) {
        view!!.scaleY = 1f
    }

    override fun handleLeftPage(view: View?, position: Float) {
        view!!.scaleY = 1f
    }

}