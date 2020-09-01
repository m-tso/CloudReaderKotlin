package com.example.cloudreaderkotloin.bussiness.collection.adapter

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.cloudreaderkotloin.R
import com.example.cloudreaderkotloin.base.BindingViewHolder
import com.example.cloudreaderkotloin.base.XRecyclerView
import com.example.cloudreaderkotloin.base.XRvAdapter
import com.example.cloudreaderkotloin.bussiness.collection.viewmodel.CollectionLoadData
import com.example.cloudreaderkotloin.bussiness.collection.viewmodel.LoadType
import com.example.cloudreaderkotloin.bussiness.collection.viewmodel.VmCollections
import com.example.cloudreaderkotloin.bussiness.common.bean.NetWorkException
import com.example.cloudreaderkotloin.bussiness.common.utils.cancleCollect
import com.example.cloudreaderkotloin.bussiness.common.utils.checkConnectIsAvailable
import com.example.cloudreaderkotloin.bussiness.common.utils.isLogin
import com.example.cloudreaderkotloin.bussiness.home.wan.activity.ArticleDetailActivity
import com.example.cloudreaderkotloin.bussiness.home.wan.activity.LoginActivity
import com.example.cloudreaderkotloin.bussiness.home.wan.bean.CollectArticle
import com.example.cloudreaderkotloin.databinding.CollectArticleBinding

class CollectArticleAdapter : XRvAdapter<BindingViewHolder<*>> {
    lateinit var ldCollectArticles: MutableLiveData<CollectionLoadData>

    lateinit var lifecycleOwner: LifecycleOwner
    lateinit var viewModel: VmCollections

    constructor(_lifecycleOwner: LifecycleOwner, vm: VmCollections) : super() {
        this.lifecycleOwner = _lifecycleOwner
        viewModel = vm

        ldCollectArticles = vm.collectionsData

        ldCollectArticles.observe(lifecycleOwner, Observer {
            when (it.loadType) {
                is LoadType.LoadFirstPage -> {
                    mRecyclerView.mViewData.clear()
                    mRecyclerView.mViewData.addAll(it.data!!)
                    mRecyclerView.mAdapter?.notifyDataSetChanged()
                    mRecyclerView.stopLoadMore()
                }

                is LoadType.LoadRefresh -> {
                    refreshData(it.data!!)
                    mRecyclerView.stopLoadMore()
                }
                is LoadType.LoadMore -> addArticle(it.data!!)
            }


        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<*> {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_collect_article, null, false)
        val binding = DataBindingUtil.bind<CollectArticleBinding>(view)!!
        val holder = BindingViewHolder(binding)
        return holder
    }

    override fun onBindViewHolder(holder: BindingViewHolder<*>, position: Int) {
        val binding = holder.binding as CollectArticleBinding

        val article = mRecyclerView.mViewData[position] as CollectArticle
        binding.article = article
        binding.root.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("link",article.link)
            bundle.putString("title",article.title)
            if (lifecycleOwner is AppCompatActivity) {
                ArticleDetailActivity.start(lifecycleOwner as AppCompatActivity, bundle)
            } else if (lifecycleOwner is Fragment) {
                ArticleDetailActivity.start(
                    ((lifecycleOwner as Fragment).context) as AppCompatActivity,
                    bundle
                )
            }
        }

        binding.ivAddCollection.setOnClickListener {
            if (checkConnectIsAvailable()){
                Log.i("cancleId",article.id.toString())
                Log.i("cancleId",article.originId.toString())
                viewModel.cancleFromCollection(article.id,article.originId)
                mRecyclerView.mViewData.remove(article)
            }else{
                Toast.makeText(mRecyclerView.context,NetWorkException.NOTCONNECT,Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun refreshData(collectionList:List<CollectArticle>){
        val newFirstCollectionArticleId = collectionList[0].id
        val isRefresh = (mRecyclerView.mViewData[0] as CollectArticle).id != newFirstCollectionArticleId
        if (!isRefresh){ return }
        mRecyclerView.mViewData.clear()
        mRecyclerView.mViewData.addAll(collectionList)
        mRecyclerView.mAdapter?.notifyDataSetChanged()



    }



private fun addArticle(article: List<CollectArticle>) {
    if(article.isEmpty()){
        mRecyclerView.tagNoMore()
        return
    }

    article.forEach {
        if (!mRecyclerView.mViewData.contains(it)) {
            mRecyclerView.mViewData.add(it)
        }
    }
}

}