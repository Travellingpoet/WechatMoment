package com.xuziq.wechat

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.util.SmartUtil
import com.xuziq.wechat.adpater.MomentAdapter
import com.xuziq.wechat.api.MomentApi
import com.xuziq.wechat.bean.MomentBean
import com.xuziq.wechat.bean.ProfileBean
import com.xuziq.wechat.http.RetrofitManager
import com.xuziq.wechat.utils.ListPageUtils
import com.xuziq.wechat.utils.RefreshUtils
import com.xuziq.wechat.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(),RefreshUtils.onRefreshListener {
    private var mOffset = 0
    private var mScrollY = 0
    private var mAdapter:MomentAdapter = MomentAdapter()
    private var mApi:MomentApi ?= null
    private var mList = arrayListOf<MomentBean>()
    private var page = 1
    private var rows = 5
    private lateinit var utils: RefreshUtils<MomentBean>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initAdapter()
        getBaseInfo()
        toolbar.setNavigationOnClickListener {
            finish()
        }

        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setMargin(this, findViewById(R.id.header))

        utils = RefreshUtils(mAdapter, refreshLayout, this,recyclerView )
        utils.onRefresh()


        scrollView.setOnScrollChangeListener(object :NestedScrollView.OnScrollChangeListener{
            private var lastScrollY = 0
            private val h: Int = SmartUtil.dp2px(170f)
            private val color: Int = ContextCompat.getColor(applicationContext, R.color.white) and 0x00ffffff
            private val textColor: Int = ContextCompat.getColor(applicationContext, R.color.textBlack) and 0x00000000
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
                    toolbar.setBackgroundColor(255 * mScrollY / h shl 24 or color)
                    tvTitle.setBackgroundColor(255 * mScrollY / h shl 24 or textColor)
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
        recyclerView.adapter = mAdapter
    }

    private fun getBaseInfo(){
        mApi = RetrofitManager.getInstance()?.getRetrofit()?.create(MomentApi::class.java)
        val call = mApi?.getUser()
        call?.enqueue(object : Callback<ProfileBean>{
            override fun onFailure(call: Call<ProfileBean>, t: Throwable) {
                Toast.makeText(this@MainActivity,"网络请求错误",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ProfileBean>, response: Response<ProfileBean>) {
                val nickname = findViewById<TextView>(R.id.nickName)
//                Glide.with(this@MainActivity).load(response.body()?.profileImage).into(parallax)
                Glide.with(this@MainActivity).load(response.body()?.avatar)
                    .transform(CenterCrop(),RoundedCorners(5))
                    .into(avatar)
                nickname.text = response.body()?.nick
            }

        })
    }
    private fun getMomentList(page:Int){
        mApi = RetrofitManager.getInstance()?.getRetrofit()?.create(MomentApi::class.java)
        val call = mApi?.getList()
        call?.enqueue(object :Callback<List<MomentBean>>{
            override fun onFailure(call: Call<List<MomentBean>>, t: Throwable) {
                Toast.makeText(this@MainActivity,"网络请求错误",Toast.LENGTH_SHORT).show()
                Log.e("=============",t.message)
                if (RefreshState.Refreshing.isOpening) {
                    utils.smartRefreshLayout.closeHeaderOrFooter()
                }
            }

            override fun onResponse(
                call: Call<List<MomentBean>>,
                response: Response<List<MomentBean>>
            ) {
                Log.e("============adapter",mAdapter.data.toString())
                //删除错误数据
                mList.clear()
                for (i in response.body()!!.indices){
                    if (response.body()?.get(i)?.error == null){
                        mList.add(response.body()?.get(i)!!)
                    }
                }

                val pager = ListPageUtils<MomentBean>(mList,rows)

                val nextList = arrayListOf<MomentBean>()
                nextList.clear()
                Log.e("============adlapter",mAdapter.data.toString())
                for (i in pager.getPageList(page)){
                    nextList.add(i)
                }
                utils.success(nextList)

            }


        })
    }


    override fun loadData() {
       getMomentList(utils.mNextRequestPage)
    }

    override fun onPause() {
        super.onPause()
        if (RefreshState.Refreshing.isOpening) {
            utils.smartRefreshLayout.closeHeaderOrFooter()
        }
    }


}
