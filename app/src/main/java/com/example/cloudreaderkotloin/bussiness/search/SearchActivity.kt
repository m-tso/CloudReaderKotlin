package com.example.cloudreaderkotloin.bussiness.search

import android.content.Context
import android.content.DialogInterface.OnClickListener
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cloudreaderkotloin.R
import com.example.cloudreaderkotloin.base.BaseActivity
import com.example.cloudreaderkotloin.base.NetWorkActivity
import com.example.cloudreaderkotloin.base.XRecyclerView
import com.example.cloudreaderkotloin.bussiness.common.bean.NetWorkException
import com.example.cloudreaderkotloin.bussiness.common.utils.*
import com.example.cloudreaderkotloin.bussiness.home.wan.adapter.WanArticleAdapter
import com.example.cloudreaderkotloin.bussiness.search.bean.WanHotSearchWord
import com.example.cloudreaderkotloin.bussiness.search.viewmodel.SearchType
import com.example.cloudreaderkotloin.bussiness.search.viewmodel.VmSearch
import com.example.cloudreaderkotloin.databinding.SearchBinding
import com.google.android.material.internal.FlowLayout
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity(override val contentViewId: Int = R.layout.activity_search) :
    NetWorkActivity<VmSearch>() {
    private var hotSearchWord: MutableLiveData<List<WanHotSearchWord>>? = null
    private var searchPage: Int = 1
    private var searchWord: String = ""
    private val pool = RecyclerView.RecycledViewPool()
    private var ldHistory: MutableLiveData<ArrayList<String>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flowStatusBar(resources.getColor(R.color.colorTheme), search_status_bar_background)
        setSupportActionBar(search_toolbar)

        viewModel?.refreshHotSearchWords()
        viewModel?.refreshHistory()
        hotSearchWord = viewModel?.hotSearchWorld
        ldHistory = viewModel?.ldHistory
        dataObserve()
        initViews()
    }


    private fun initViews() {
        rv_search_result.layoutManager =
            WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_search_result.setXRvAdapter(WanArticleAdapter(this, viewModel!!))
        rv_search_result.openLoadMoreView()

        pool.setMaxRecycledViews(0, 20);
        rv_search_result.setRecycledViewPool(pool);
        img_search_back.setOnClickListener { finish() }

        rv_search_result.onStartLoadMoreListener = {
            viewModel?.getWanArticleByWords(searchPage, searchWord, SearchType.SearchSameWord())
            searchPage++
        }


        edt_search_content.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                val content: String = edt_search_content.text.toString().trim()
                if (TextUtils.isEmpty(content)) {
                    rv_search_result.mViewData.clear()
                    rv_search_result.mAdapter?.notifyDataSetChanged()
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val content: String = edt_search_content.text.toString().trim()
                searchWord = content
                if (!TextUtils.isEmpty(content)) {
                    viewModel?.getWanArticleByWords(0, content, SearchType.SearchWordAppend())
                    showSearchResult(true)
                    iv_clear_words.visibility = View.VISIBLE
                } else {
                    showSearchResult(false)
                    iv_clear_words.visibility = View.GONE
                }
            }

        })

        edt_search_content.setOnEditorActionListener { v, actionId, event ->
            if ((actionId == 0 || actionId == 3) && event != null) {
                val content: String = edt_search_content.text.toString().trim()
                if (!TextUtils.isEmpty(content)) {
                    viewModel?.getWanArticleByWords(0, content, SearchType.SearchNewWord())
                    searchWord = content
                    showSearchResult(true)
                } else {
                    showSearchResult(false)
                }
                true
            }
            false

        }

        iv_clear_words.setOnClickListener {
            edt_search_content.setText("")
            showSearchResult(false)
            rv_search_result.mViewData.clear()
            rv_search_result.mAdapter?.notifyDataSetChanged()
            it.visibility = View.GONE
        }

        iv_history_delete.setOnClickListener {
            showSimpleDialog(it,"确认清空全部历史记录？","清空","取消",
                OnClickListener { dialog, which ->
                    viewModel?.clearSearchHistory()
                })
        }



    }

    private fun dataObserve() {
        hotSearchWord?.observe(this, Observer {
            it?.let { paddingFlowLayout(flt_hot_search, it) }
        })

        ldHistory?.observe(this, Observer {
            if (it.isEmpty()){
                ll_search_history.visibility = View.GONE
            }else{
                ll_search_history.visibility = View.VISIBLE
                paddingFlowLayout(flt_search_history,it)
            }

        })
    }

    private fun <T> paddingFlowLayout(flowLayout: FlowLayout, words: List<T>) {
        flowLayout.removeAllViews()

        words.forEach { it ->
            val textView = LayoutInflater.from(flowLayout.context)
                .inflate(R.layout.view_flowlayout_tab, null) as TextView
            var txt = ""
            if (it is WanHotSearchWord) {
                txt = it.name
            } else if (it is String) {
                txt = it
            }

            textView.text = txt

            textView.setOnClickListener { ite ->
                if (!checkConnectIsAvailable()){
                    Toast.makeText(this,NetWorkException.NOTCONNECT,Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                viewModel?.getWanArticleByWords(0, txt, SearchType.SearchNewWord())
                showSearchResult(true)
                searchWord = txt
                edt_search_content?.setText(txt)
                setTextCursor(edt_search_content)
                hideSoftKeyBoard(this)

            }
            flowLayout.addView(textView)

        }
    }

    fun showSearchResult(show: Boolean) {
        if (show ) {
            if (rv_search_result.visibility != View.VISIBLE){
                rv_search_result.visibility = View.VISIBLE
                ll_search_history.visibility = View.GONE
                llt_hot_search.visibility = View.GONE
            }
        } else {
            rv_search_result.visibility = View.GONE
            ll_search_history.visibility = View.VISIBLE
            llt_hot_search.visibility = View.VISIBLE
            viewModel?.refreshHotSearchWords()
            viewModel?.refreshHistory()
        }
    }


}