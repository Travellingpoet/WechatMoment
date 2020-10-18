package com.xuziq.wechat.utils

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

class RefreshUtils<T>(var mAdapter: BaseQuickAdapter<T, BaseViewHolder>, val smartRefreshLayout: RefreshLayout
                      , var listener: onRefreshListener? = null, val mRecyclerView: RecyclerView,
                      loadMore: Boolean = true)
    : OnRefreshLoadMoreListener {

    lateinit var emptyViews: View
    override fun onRefresh(refreshLayout:RefreshLayout) {
        mNextRequestPage = 1
        mAdapter.isUseEmpty(true)
        smartRefreshLayout.setNoMoreData(false)
        listener?.loadData()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        listener?.loadData()
    }

    init {
        isEnableLoadMore(loadMore)
        //是否启用加载更多
        smartRefreshLayout.setEnableAutoLoadMore(true)
        smartRefreshLayout.setOnRefreshListener(this)
        smartRefreshLayout.setOnLoadMoreListener(this)
    }

    fun isEnableLoadMore(loadMore: Boolean) {
        smartRefreshLayout.setEnableRefresh(loadMore)
    }

    fun isRefresh(isRefresh: Boolean) {
        smartRefreshLayout.setEnableRefresh(isRefresh)
    }


    companion object {
        var PAGE_SIZE: Int = 5
    }

    var mNextRequestPage: Int = 1

    /**
     * 刷新
     */
    fun onRefresh(clear: Boolean = false) {
        if (clear)
            mAdapter.setNewData(ArrayList<T>())

        smartRefreshLayout.autoRefresh()
    }

    fun success(data: List<T>?) {

        //刷新
        if (smartRefreshLayout.state == RefreshState.Refreshing) {
                smartRefreshLayout.finishRefresh(100)
                mAdapter.setNewData(data)
            } else {
                smartRefreshLayout.finishLoadMore()
                data?.let {
                    Log.e("===========",mAdapter.data.toString())
                    mAdapter.addData(it)
                }
        }

        if (data == null || data.isEmpty()) {
            smartRefreshLayout.finishLoadMoreWithNoMoreData()
        } else {
            mNextRequestPage++
        }
    }


    /**
     * 数据不分页
     */
    fun success(data: List<T>?, isPaging: Boolean) {

        //刷新
        if (smartRefreshLayout.state == RefreshState.Refreshing) {
            smartRefreshLayout.finishRefresh(100)
            mAdapter.setNewData(data)
        } else {
            if (!isPaging) {
                smartRefreshLayout.finishLoadMore(500)
                data?.let {
                    mAdapter.setNewData(it)
                }
            }
        }

        if (data == null || data.isEmpty()) {
            smartRefreshLayout.finishLoadMoreWithNoMoreData()
        } else {
            mNextRequestPage++
        }
    }

    fun failure() {
        //刷新
        if (smartRefreshLayout.state == RefreshState.Refreshing || smartRefreshLayout.state == RefreshState.RefreshFinish) {
            smartRefreshLayout.finishRefresh(100, true,false)
            mAdapter.setNewData(ArrayList<T>())
        } else {
            smartRefreshLayout.finishLoadMore(100, false, false)
        }
    }

    interface onRefreshListener {
        fun loadData()
    }


}