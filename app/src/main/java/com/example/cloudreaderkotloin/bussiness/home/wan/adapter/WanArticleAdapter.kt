package com.example.cloudreaderkotloin.bussiness.home.wan.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.cloudreaderkotloin.R
import com.example.cloudreaderkotloin.base.BindingViewHolder
import com.example.cloudreaderkotloin.base.XRvAdapter
import com.example.cloudreaderkotloin.bussiness.common.utils.*
import com.example.cloudreaderkotloin.bussiness.home.wan.activity.ArticleDetailActivity
import com.example.cloudreaderkotloin.bussiness.home.wan.activity.LoginActivity
import com.example.cloudreaderkotloin.bussiness.home.wan.bean.Article
import com.example.cloudreaderkotloin.bussiness.search.viewmodel.SearchType
import com.example.cloudreaderkotloin.bussiness.search.viewmodel.VmSearch
import com.example.cloudreaderkotloin.bussiness.search.viewmodel.WanSearchData
import com.example.cloudreaderkotloin.databinding.WanArticleBinding

class WanArticleAdapter : XRvAdapter<BindingViewHolder<*>> {
    lateinit var ldArticles: MutableLiveData<WanSearchData>

    lateinit var lifecycleOwner: LifecycleOwner
    lateinit var viewModel: VmSearch

    constructor(_lifecycleOwner: LifecycleOwner, vm: VmSearch) : super() {
        this.lifecycleOwner = _lifecycleOwner
        viewModel = vm

        ldArticles = vm.wanSearchLiveData

        ldArticles.observe(lifecycleOwner, Observer {
            when (it.searchType) {
                is SearchType.SearchNewWord -> {
                    mRecyclerView.mViewData.clear()
                    mRecyclerView.mViewData.addAll(it.data!!)
                    mRecyclerView.mAdapter?.notifyDataSetChanged()
                    mRecyclerView.stopLoadMore()
                }

                is SearchType.SearchWordAppend -> filterArticle(it.data!!)

                is SearchType.SearchSameWord -> addArticle(it.data!!)
            }


        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<*> {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_wan_article, null, false)
        val binding = DataBindingUtil.bind<WanArticleBinding>(view)!!
        val holder = BindingViewHolder(binding)
        return holder
    }

    override fun onBindViewHolder(holder: BindingViewHolder<*>, position: Int) {
        val binding = holder.binding as WanArticleBinding

        val article = mRecyclerView.mViewData[position] as Article
        binding.article = article
        binding.root.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("link",article.link)
            bundle.putString("title",article.title)
            if (lifecycleOwner is AppCompatActivity) {
                (lifecycleOwner as AppCompatActivity)
                    .startMyActivity<ArticleDetailActivity>(bundle)

            } else if (lifecycleOwner is Fragment) {
                (lifecycleOwner as Fragment).startMyActivity<ArticleDetailActivity>(bundle)
            }
        }

        binding.ivAddCollection.setOnClickListener {
            if (!isLogin()){
                val bundle = Bundle()
                bundle.putInt("articleId",article.id)
                (lifecycleOwner as AppCompatActivity).startMyActivity<LoginActivity>(bundle)

                return@setOnClickListener
            }

            val isChecked = binding.ivAddCollection.isChecked
            if (!checkConnectIsAvailable(mRecyclerView.context)){
                binding.ivAddCollection.isChecked = !isChecked
                return@setOnClickListener
            }

            if (isChecked){
                addArticle2Collections(article.id,viewModel,viewModel.add2Collect)
            }else{
                cancleCollect(article.id,viewModel,viewModel.cancleCollect)
            }
        }
    }




    private fun addArticle(article: List<Article>) {
        if(article.isEmpty()){
            mRecyclerView.tagNoMore()
            return
        }
        mRecyclerView.stopLoadMore()
        article.forEach {
            if (!mRecyclerView.mViewData.contains(it)) {
                mRecyclerView.mViewData.add(it)
            }
        }
    }

    private fun filterArticle(article: List<Article>) {
        mRecyclerView.stopLoadMore()
        mRecyclerView.mViewData.clear()
        mRecyclerView.mViewData.addAll(article)
        mRecyclerView.mAdapter?.notifyDataSetChanged()


    }
}