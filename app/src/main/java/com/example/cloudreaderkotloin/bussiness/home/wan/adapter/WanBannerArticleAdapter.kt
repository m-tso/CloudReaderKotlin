package com.example.cloudreaderkotloin.bussiness.home.wan.adapter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.cloudreaderkotloin.R
import com.example.cloudreaderkotloin.base.BaseFragment
import com.example.cloudreaderkotloin.base.BindingViewHolder
import com.example.cloudreaderkotloin.base.XRvAdapter
import com.example.cloudreaderkotloin.bussiness.common.utils.*
import com.example.cloudreaderkotloin.bussiness.home.wan.activity.ArticleDetailActivity
import com.example.cloudreaderkotloin.bussiness.home.wan.activity.LoginActivity
import com.example.cloudreaderkotloin.bussiness.home.wan.bean.Article
import com.example.cloudreaderkotloin.bussiness.home.wan.bean.WanBanner
import com.example.cloudreaderkotloin.bussiness.home.wan.viewmodel.VmWanMain
import com.example.cloudreaderkotloin.bussiness.home.wan.viewmodel.WanMainLiveData
import com.example.cloudreaderkotloin.databinding.WanArticleBinding
import com.example.cloudreaderkotloin.databinding.WanBannerBinding
import com.stx.xhb.xbanner.XBanner

/**
 * @Description: 玩安卓首页适配器，加载包括banner和article
 * @Author: tso 2020/8/6 11:24
 **/
class WanBannerArticleAdapter : XRvAdapter<BindingViewHolder<*>> {

    private var model: VmWanMain
    private var fragment: BaseFragment<*, *>
    //首页初始化的数据，包含banner和article的第一页数据
    private var ldWanMainData: MutableLiveData<WanMainLiveData>
    //用来获取各页article的数据
    private lateinit var ldPageArticle: MutableLiveData<List<Article>>

    private var headerBanner: XBanner ?= null           //头部banner
    private var lsBannerImage = ArrayList<String>()     //banner的图片

    var page = 1
    private val ARTICLE = 1
    private val BANNER = 0



    /**
     * @Description:
     * @Author: tso 2020/8/6 11:26
     * @param _fragment 用于获取提供数据的viewmodel对象和context
     */
    constructor(_fragment: BaseFragment<*, *>) : super() {
        this.fragment = _fragment
        model = fragment.viewModel as VmWanMain
        ldWanMainData = model.getWanFirstPageData()
        ldPageArticle = model.specifyPageArticle

        ldWanMainData.observe(fragment, Observer {
            if (!it.updateAll){
                updatePartData(it.lsBannerAndArticles)
                return@Observer
            }

            mRecyclerView.mViewData.clear()
            lsBannerImage.clear()

            mRecyclerView.mViewData.addAll(it.lsBannerAndArticles)
            val lsBanner = mRecyclerView.mViewData[0] as ArrayList<WanBanner>
            lsBannerImage.addAll(lsBanner.map { banner-> banner.imagePath })
            mRecyclerView.mAdapter?.notifyDataSetChanged()
        })

        ldPageArticle.observe(fragment, Observer {
            val insertPosition = mRecyclerView.mViewData.size
            mRecyclerView.mViewData.addAll(it)
            notifyItemInserted(insertPosition?:0)
            mRecyclerView.stopLoadMore()
        })

        model.cancleCollect.observe(fragment, Observer {

        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<*> {
        var view: View
        var holder: BindingViewHolder<*>

        if (viewType == BANNER) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_wan_banner,parent,false)
            val binding = DataBindingUtil.bind<WanBannerBinding>(view)!!
            holder = BindingViewHolder(binding)

        } else {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_wan_article, null, false)
            val binding = DataBindingUtil.bind<WanArticleBinding>(view)!!
            holder = BindingViewHolder(binding)
        }

        holder.tag = viewType
        return holder
    }

    override fun onBindViewHolder(holder: BindingViewHolder<*>, position: Int) {
        if (holder.tag == ARTICLE) {
            val binding = holder.binding as WanArticleBinding
            val article = mRecyclerView.mViewData[position] as Article
            binding.article = article
            binding.root.setOnClickListener{
                val bundle = Bundle()
                bundle.putString("link",article.link)
                bundle.putString("title",article.title)

                fragment.startMyActivity<ArticleDetailActivity>(bundle)

                Log.i("articleid",article.id.toString())
                Log.i("articleid",article.collect.toString())
            }

            binding.ivAddCollection.setOnClickListener {
                if (!isLogin()){
                    val intent = Intent(fragment.activity,LoginActivity::class.java)
                    intent.putExtra("articleId",article.id)

                    fragment.activity?.startActivity(intent)
                    return@setOnClickListener
                }

                val isChecked = binding.ivAddCollection.isChecked

                if (!checkConnectIsAvailable(mRecyclerView.context)){
                    binding.ivAddCollection.isChecked = !isChecked
                    return@setOnClickListener
                }

                if (isChecked){
                    addArticle2Collections(article.id,model,model.add2Collect)
                }else{
                    cancleCollect(article.id,model,model.cancleCollect)
                }
            }

        } else if (holder.tag == BANNER){
            val headerBinding = holder.binding as WanBannerBinding
            if (lsBannerImage.size > 0){
                headerBinding.radioGroup.visibility = View.VISIBLE
            }

            headerBanner = headerBinding.xbanner
            headerBinding.xbanner.setBannerData(lsBannerImage)
            headerBinding.xbanner.setIsClipChildrenMode(true)
            if (fragment.activity != null) {
                headerBinding.xbanner.loadImage(XBanner.XBannerAdapter { banner, model, view, position ->
                    Glide.with(fragment.activity!!).load(model as String).into(view as ImageView)
                })

                //去掉banner默认的切换动画，同时开启手动无线循环和自动播放，并启动循环，必须同时在xml和代码中同时设置才能生效
                headerBinding.xbanner.setCustomPageTransformer(NormalPageTransformer())
                headerBinding.xbanner.setHandLoop(true)
                headerBinding.xbanner.setAutoPlayAble(true)
                headerBinding.xbanner.startAutoPlay()
            }
        }
    }

    private fun updatePartData(articles: ArrayList<Any>){
        val size = articles.size
        //非完全更新，第一个元素无非再次检查
        for (i in 1 until size){
            if (articles[i] as Article != mRecyclerView.mViewData[i] as Article){
                mRecyclerView.mViewData[i] = articles[i]
            }
        }

    }

    fun loadNewPageArticles(){
        model.loadArticleByPage(page)
        page++
    }



    override fun getItemViewType(position: Int): Int {
        return if (position == 0) BANNER else ARTICLE
    }

    fun getBannerView(): XBanner?{
        return headerBanner
    }
}