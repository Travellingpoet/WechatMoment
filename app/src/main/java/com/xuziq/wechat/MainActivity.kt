package com.xuziq.wechat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.simple.SimpleMultiListener
import com.scwang.smart.refresh.layout.util.SmartUtil
import com.xuziq.wechat.adpater.MomentAdapter
import com.xuziq.wechat.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var mOffset = 0
    private var mScrollY = 0
    private var mAdapter:MomentAdapter ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initAdapter()
        toolbar.setNavigationOnClickListener {
            finish()
        }

        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setMargin(this, findViewById(R.id.header))

        refreshLayout.setOnMultiListener(object :SimpleMultiListener(){
            override fun onRefresh(refreshLayout: RefreshLayout) {
                super.onRefresh(refreshLayout)
                refreshLayout.finishRefresh(300)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                super.onLoadMore(refreshLayout)
                refreshLayout.finishLoadMore(200)
            }

            override fun onHeaderMoving(
                header: RefreshHeader?,
                isDragging: Boolean,
                percent: Float,
                offset: Int,
                headerHeight: Int,
                maxDragHeight: Int
            ) {
                mOffset = offset / 2
                parallax.translationY = mOffset - mScrollY.toFloat()
            }
        })

        scrollView.setOnScrollChangeListener(object :NestedScrollView.OnScrollChangeListener{
            private var lastScrollY = 0
            private val h: Int = SmartUtil.dp2px(170f)
            private val color: Int = ContextCompat.getColor(applicationContext, R.color.colorPrimary) and 0x00ffffff
            override fun onScrollChange(
                v: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (lastScrollY < h) {
                    val absScrollY = h.coerceAtMost(scrollY)
                    mScrollY = if (absScrollY > h) h else absScrollY
                    parallax.translationY = mOffset - mScrollY.toFloat()
                }
                lastScrollY = scrollY
            }
        })
        tvTitle.alpha = 0f
        toolbar.setBackgroundColor(0)
    }

    private fun initAdapter(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = MomentAdapter()
        recyclerView.adapter = mAdapter
    }
}
