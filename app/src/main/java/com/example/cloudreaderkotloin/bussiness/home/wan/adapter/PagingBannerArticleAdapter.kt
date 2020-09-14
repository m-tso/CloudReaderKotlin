package com.example.cloudreaderkotloin.bussiness.home.wan.adapter


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.cloudreaderkotloin.R
import com.example.cloudreaderkotloin.base.BaseFragment
import com.example.cloudreaderkotloin.base.BindingViewHolder
import com.example.cloudreaderkotloin.bussiness.common.utils.*
import com.example.cloudreaderkotloin.bussiness.home.wan.activity.ArticleDetailActivity
import com.example.cloudreaderkotloin.bussiness.home.wan.activity.LoginActivity
import com.example.cloudreaderkotloin.bussiness.home.wan.bean.Article
import com.example.cloudreaderkotloin.bussiness.home.wan.bean.WanBannerRequestData
import com.example.cloudreaderkotloin.bussiness.home.wan.viewmodel.VmWanMain
import com.example.cloudreaderkotloin.databinding.WanArticleBinding
import com.example.cloudreaderkotloin.databinding.WanBannerBinding
import com.stx.xhb.xbanner.XBanner


class PagingBannerArticleAdapter : PagingDataAdapter<Any, BindingViewHolder<*>> {
    private var headerBanner: XBanner? = null           //头部banner
    private var lsBannerImage = ArrayList<String>()     //banner的图片
    private lateinit var fragment: BaseFragment<*, *>
    private var model: VmWanMain


    companion object {
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {

                return if (oldItem is Article && newItem is Article) {
                    oldItem.id == newItem.id
                } else if ( oldItem is WanBannerRequestData && newItem is WanBannerRequestData) {
                    oldItem as WanBannerRequestData == newItem as WanBannerRequestData
                }else{
                    false
                }

            }

            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                return if (oldItem is Article && newItem is Article) {
                    oldItem as Article == newItem as Article

                } else if (oldItem is WanBannerRequestData && newItem is WanBannerRequestData){
                    oldItem as WanBannerRequestData == newItem as WanBannerRequestData
                }else{
                    false
                }

            }

        }
    }

    constructor(_fragment: BaseFragment<*, *>) : super(POST_COMPARATOR) {
        fragment = _fragment
        model = fragment.viewModel as VmWanMain
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<*> {
        if (viewType == 1) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_wan_article, null, false)

            return BindingViewHolder<WanArticleBinding>(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_wan_banner, parent, false)

            return BindingViewHolder<WanBannerBinding>(view)
        }


    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 0 else 1
    }


    override fun onBindViewHolder(holder: BindingViewHolder<*>, position: Int) {
        if (position == 0) {
            val headerBinding = holder.binding as WanBannerBinding
            val banners = (getItem(0) as WanBannerRequestData).data

            if (banners.isNotEmpty()) {
                headerBinding.radioGroup.visibility = View.VISIBLE
            }

            lsBannerImage.clear()
            lsBannerImage.addAll(banners.map { banner-> banner.imagePath })

            headerBanner = headerBinding.xbanner
            headerBinding.xbanner.setBannerData(lsBannerImage)
            headerBinding.xbanner.setIsClipChildrenMode(true)

            headerBinding.xbanner.loadImage(XBanner.XBannerAdapter { banner, model, view, position ->
                Glide.with(fragment.context!!).load(model as String).into(view as ImageView)
            })

            //去掉banner默认的切换动画，同时开启手动无线循环和自动播放，并启动循环，必须同时在xml和代码中同时设置才能生效
            headerBinding.xbanner.setCustomPageTransformer(NormalPageTransformer())
            headerBinding.xbanner.setHandLoop(true)
            headerBinding.xbanner.setAutoPlayAble(true)
            headerBinding.xbanner.startAutoPlay()
        } else {
            val binding = holder.binding as WanArticleBinding
            val article = getItem(position) as Article
            binding.article = article

            binding.root.setOnClickListener{
                val bundle = Bundle()
                bundle.putString("link",article.link)
                bundle.putString("title",article.title)

                fragment.startMyActivity<ArticleDetailActivity>(bundle)

            }

            binding.ivAddCollection.setOnClickListener {
                if (!isLogin()){
                    val intent = Intent(fragment.activity, LoginActivity::class.java)
                    intent.putExtra("articleId",article.id)

                    fragment.activity?.startActivity(intent)
                    binding.ivAddCollection.isChecked = false
                    return@setOnClickListener
                }

                val isChecked = binding.ivAddCollection.isChecked

                if (!checkConnectIsAvailable(fragment.context)){
                    binding.ivAddCollection.isChecked = !isChecked
                    return@setOnClickListener
                }

                if (isChecked){
                    addArticle2Collections(article.id,model,model.add2Collect)
                }else{
                    cancleCollect(article.id,model,model.cancleCollect)
                }
            }

        }
    }


}


