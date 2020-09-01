package com.example.cloudreaderkotloin.base

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList.OnListChangedCallback
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.cloudreaderkotloin.R
import java.lang.Exception

/**
 * @Description: 封装recyclerview
 * @Author: tso 2020/8/31 14:46
 */
class XRecyclerView : RecyclerView {

    var mBloadMoreView: View? = null
    var mLoadMoreLayoutId = -1
    var mIsLoadingData = false
    private var mIsOpenLoadMoreMode = false
    val mViewData = ObservableArrayList<Any>()

    var mAdapter: WrapAdapter? = null

    var onStartLoadMoreListener: ((View) -> Unit) ? = null
    var onLoadMoreFinishListener: ((View) -> Unit) ? = null
    var onNoMoreDataListener: ((View) -> Unit) ? = null


    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    )



    override fun onScrollStateChanged(newState: Int) {
        super.onScrollStateChanged(newState)
        var lastPosition = -1
        var firstPosition = 0
        val layoutManager: LayoutManager = layoutManager!!

        //当前状态为停止滑动状态SCROLL_STATE_IDLE时
        if (newState === RecyclerView.SCROLL_STATE_IDLE) {

            if (layoutManager is GridLayoutManager) {
                //通过LayoutManager找到当前显示的最后的item的position
                lastPosition = layoutManager.findLastVisibleItemPosition()
                firstPosition = layoutManager.findFirstVisibleItemPosition()

            } else if (layoutManager is LinearLayoutManager) {
                lastPosition = layoutManager.findLastVisibleItemPosition()
                firstPosition = layoutManager.findFirstVisibleItemPosition()

            } else if (layoutManager is StaggeredGridLayoutManager) {
                //因为StaggeredGridLayoutManager的特殊性可能导致最后显示的item存在多个，所以这里取到的是一个数组
                //得到这个数组后再取到数组中position值最大的那个就是最后显示的position值了

                val lastPositions = IntArray(layoutManager.spanCount)
                layoutManager.findLastVisibleItemPositions(lastPositions)

                lastPosition = findMax(lastPositions)

                val firstPositions = IntArray(layoutManager.spanCount)
                layoutManager.findFirstVisibleItemPositions(firstPositions)

                firstPosition = firstPositions.min()!!
            }

            //时判断界面显示的最后item的position是否等于itemCount总数-1也就是最后一个item的position
            //如果相等则说明已经滑动到最后了。如果所有的item不足一页，那也不用显示加载更多了
            if (lastPosition == getLayoutManager()!!.itemCount - 1
                && mViewData.size != 0 && firstPosition != 0
            ) {
                startLoadMore()
            }
        }
    }

    //找到数组中的最大值
    private fun findMax(lastPositions: IntArray): Int {
        var max = lastPositions[0]
        for (value in lastPositions) {
            if (value > max) {
                max = value
            }
        }
        return max
    }

    fun openLoadMoreView(layoutId: Int = R.layout.view_load_more) {
        mLoadMoreLayoutId = layoutId
        mIsOpenLoadMoreMode = true
    }

    /**
     * @Description: 标记无更多数据，默认情况下会recyclerview最下方item显示为“没有更多数据了”
     * @Author: tso 2020/9/1 14:00
     */
    fun tagNoMore() {
        if ( mLoadMoreLayoutId == R.layout.view_load_more ) {
            setLoadMoreType(LoadMoreType.NoMore)
        }
        mBloadMoreView?.let { onNoMoreDataListener?.let { it1 -> it1(it) } }
    }

    private fun setLoadMoreType(type: LoadMoreType) {

        val textView = mBloadMoreView?.findViewById<TextView>(R.id.tv_no_more_tag)
        val drawableAnimation = mBloadMoreView?.findViewById<ImageView>(R.id.loadmore_animation)

            when(type){
                is LoadMoreType.Loading -> {
                    textView?.visibility = View.GONE
                    drawableAnimation?.visibility = View.VISIBLE

                }

                is LoadMoreType.Finish -> {
                    textView?.visibility = View.GONE
                    drawableAnimation?.visibility = View.GONE
                }

                is LoadMoreType.NoMore -> {
                    drawableAnimation?.visibility = View.GONE
                    textView?.visibility = View.VISIBLE


                }
            }

    }


    /**
     * @Description: 开始加载更多，该方法有recyclerview自行调用，使用者可以通过暴露的LoadMoreListener来添加
     *               自己的代码
     * @Author: tso 2020/9/1 14:00
     */
    private fun startLoadMore() {
        if (mLoadMoreLayoutId == R.layout.view_load_more ) {
            (mBloadMoreView?.findViewById<ImageView>(R.id.loadmore_animation)?.drawable
                    as AnimationDrawable).start()
            setLoadMoreType(LoadMoreType.Loading)
        }
        onStartLoadMoreListener?.let { mBloadMoreView?.let { it1 -> it(it1) } }
    }

    /**
     * @Description: 我不知道用户什么时候数据加载完，所以此方法应由使用者自行调用
     * @Author: tso 2020/8/11 10:44
     */

    fun stopLoadMore(rewriteBlock: ((View) -> Unit)? = null) {

        if (rewriteBlock == null && mLoadMoreLayoutId == R.layout.view_load_more ) {
            (mBloadMoreView?.findViewById<ImageView>(R.id.loadmore_animation)?.drawable
                    as AnimationDrawable).stop()

            setLoadMoreType(LoadMoreType.Finish)
        }

        onLoadMoreFinishListener?.let { mBloadMoreView?.let { it1 -> it(it1) } }

    }



    fun setXRvAdapter(adapter: XRvAdapter<*>) {
        adapter.mRecyclerView = this
        mAdapter = WrapAdapter(adapter)
        super.setAdapter(mAdapter)
        mViewData.addOnListChangedCallback(object :
            OnListChangedCallback<ObservableArrayList<Any?>>() {
            override fun onChanged(sender: ObservableArrayList<Any?>?) {
                mAdapter?.notifyDataSetChanged()
            }

            override fun onItemRangeChanged(
                sender: ObservableArrayList<Any?>?,
                positionStart: Int,
                itemCount: Int
            ) {
                mAdapter?.notifyItemRangeChanged(positionStart, itemCount)
            }

            override fun onItemRangeInserted(
                sender: ObservableArrayList<Any?>?,
                positionStart: Int,
                itemCount: Int
            ) {
                mAdapter?.notifyItemRangeInserted(positionStart, itemCount)
            }

            override fun onItemRangeMoved(
                sender: ObservableArrayList<Any?>?,
                fromPosition: Int,
                toPosition: Int,
                itemCount: Int
            ) {

                mAdapter?.notifyItemMoved(fromPosition, toPosition)
            }

            override fun onItemRangeRemoved(
                sender: ObservableArrayList<Any?>?,
                positionStart: Int,
                itemCount: Int
            ) {
                mAdapter?.notifyItemRangeRemoved(positionStart, itemCount)
            }

        })
    }

    /**
     * @Description: “加载更多”状态的封装类，Loading：加载中，Finish：加载完成 NoMore：无更多数据
     * @Author: tso 2020/8/31 15:21
     */
    sealed class LoadMoreType{
        object Loading : LoadMoreType()
        object Finish : LoadMoreType()
        object NoMore: LoadMoreType()

    }


    inner class WrapAdapter : Adapter<RecyclerView.ViewHolder> {
        val LOAD_MORE_VIEWTYPE = 10000          //加载更多的viewtype
        var adapter: XRvAdapter<*>


        constructor(_adapter: XRvAdapter<*>) {
            adapter = _adapter

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            if (viewType == LOAD_MORE_VIEWTYPE && mLoadMoreLayoutId == R.layout.view_load_more) {
                mBloadMoreView =
                    LayoutInflater.from(context).inflate(mLoadMoreLayoutId, parent, false)
//                mBloadMoreView?.visibility = View.GONE
                return BaseViewHolder(mBloadMoreView!!)
            }
            return adapter.createViewHolder(parent, viewType)
        }

        override fun getItemViewType(position: Int): Int {
            if (position < mViewData.size) {
                return adapter.getItemViewType(position)
            }
            return LOAD_MORE_VIEWTYPE
        }

        override fun getItemCount(): Int {
            return if (mIsOpenLoadMoreMode) mViewData.size + 1
            else mViewData.size
        }


        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if (position < mViewData.size) {
                adapter.onBindViewHolder(holder as BindingViewHolder<*>, position)
            }
        }
    }



}