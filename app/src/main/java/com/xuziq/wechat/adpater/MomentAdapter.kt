package com.xuziq.wechat.adpater

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.android.flexbox.FlexboxLayout
import com.xuziq.wechat.R
import com.xuziq.wechat.bean.ImageList
import com.xuziq.wechat.bean.MomentBean
import com.xuziq.wechat.utils.CommonUtils
import kotlinx.android.synthetic.main.item_list_moment.view.*
import kotlinx.android.synthetic.main.view_commet.view.*
import java.lang.Exception

/**
 *
 * @Description:     java类作用描述
 * @Author:         xuziq
 * @CreateDate:     2020/10/17 0:31
 * @UpdateDate:     2020/10/17 0:31
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
 class MomentAdapter:BaseQuickAdapter<MomentBean,BaseViewHolder>(R.layout.item_list_moment){

    override fun convert(helper: BaseViewHolder, item: MomentBean?) {
        helper.setGone(R.id.content,item?.content?.isEmpty() == false)
            .setGone(R.id.frameReply, item?.comments?.isNotEmpty() == true)
            .setText(R.id.nickName,item?.sender?.nick)
            .setText(R.id.content,item?.content)
        Glide.with(mContext).load(item?.sender?.avatar)
            .transform(CenterCrop(), RoundedCorners(10))
            .placeholder(R.mipmap.img_head_pic)
            .into(helper.getView(R.id.imgHeadPic))

        if ((item?.comments?.isNotEmpty() == true && item?.comments.size != 0)) {
            helper.getView<FrameLayout>(R.id.frameReply).removeAllViews()
            addView(helper, item)
        }

        var a: Int = item?.images?.size ?: 0
        var mDatas: MutableList<String> = ArrayList()
        val str: List<ImageList?>? = item?.images

        if (a > 9) {
            a = 9
        }
        //添加数据
        for (index in 0 until a) {
            mDatas.add(str?.get(index)?.url?:"")
        }
        initImage(helper.itemView.recyclerImg,a,mDatas)

    }

    private fun addView(helper: BaseViewHolder, data: MomentBean?){
        val mCommentAdapter = CommentAdapter()

        val convertView = LayoutInflater.from(mContext).inflate(R.layout.view_commet, null, false)
        val contentView = convertView.findViewById<RecyclerView>(R.id.rvSubComment)
        helper.getView<FrameLayout>(R.id.frameReply).addView(convertView)

        convertView.rvSubComment.layoutManager = LinearLayoutManager(mContext)
        convertView.rvSubComment.adapter = mCommentAdapter

        if(data?.comments?.isNotEmpty() == true && data.comments.size != 0){
            contentView.visibility = View.VISIBLE
        }else{
            contentView.visibility = View.GONE
        }
        mCommentAdapter.setNewData(data?.comments)
    }

    private fun loadImage(image: ImageView?, urlList: MutableList<String>, index: Int, type: Int) {
        if (image != null) {
            if (type == 1) {//一张图
                Glide.with(mContext)
                    .load(urlList[index])
                    .skipMemoryCache(false)
                    .placeholder(R.mipmap.img_background)
                    .transform(CenterCrop(),RoundedCorners(5))
                    .into(image)
            } else {
                Glide.with(mContext)
                    .load(urlList[index])
                    .skipMemoryCache(true)
                    .transform(CenterCrop(),RoundedCorners(5))
                    .placeholder(R.mipmap.img_head_pic)
                    .into(image)
            }
            image.setOnClickListener {
              Toast.makeText(mContext,"点击了图片$index",Toast.LENGTH_SHORT).show()
            }
        }
    }
    /**
     * 加载9宫格图片
     */
    private fun initImage(flexboxLayout: FlexboxLayout, number: Int, mDatas: MutableList<String>) {
        flexboxLayout.removeAllViews()

        for (index in 0 until number) {
            val imageView = ImageView(mContext)
            flexboxLayout.addView(imageView)
            if (number == 4) {
                flexboxLayout.layoutParams.width =
                    (220f * CommonUtils.getScreenWidth(mContext) / 375f).toInt()
            } else {
                flexboxLayout.layoutParams.width =
                    (299f * CommonUtils.getScreenWidth(mContext) / 375f).toInt()
            }
            val lp: FlexboxLayout.LayoutParams? =
                imageView.layoutParams as FlexboxLayout.LayoutParams?
            lp?.bottomMargin = CommonUtils.dip2px(mContext, 2.5f)
            lp?.rightMargin = CommonUtils.dip2px(mContext, 2.5f)
            when (number) {
                1 -> {
                    if (imageView != null) {
                        var width: Int
                        var height: Int
                        try {
                            width = Uri.parse(mDatas[index]).getQueryParameter("w")?.toInt() ?: 0
                            height = Uri.parse(mDatas[index]).getQueryParameter("h")?.toInt()?:0
                            if (width > height) {//横屏
                                lp?.width = CommonUtils.dip2px(mContext, 299f)
                                lp?.height = 720 * CommonUtils.dip2px(mContext, 299f) / 1080
                            } else {
                                lp?.width = CommonUtils.dip2px(mContext, 170f)
                                lp?.height = height * CommonUtils.dip2px(mContext, 170f) / width
                            }

                        } catch (e: Exception) {
                            //异常
                            lp?.width = (272f * CommonUtils.getScreenWidth(mContext) / 750f).toInt()
                            lp?.height = (356f * lp?.width!! / 272f).toInt()
                        }
                        imageView.layoutParams = lp
                        imageView.scaleType = ImageView.ScaleType.FIT_XY
                        loadImage(imageView, mDatas, index, 1)
                    }
                }
                2 -> {
                    val width = (CommonUtils.getScreenWidth(mContext) - CommonUtils.dip2px(
                        mContext,
                        98f
                    )) / 2
                    lp?.width = width
                    lp?.height = width
                    loadImage(imageView, mDatas, index, 0)
                }
                3, 4, 5, 6, 7, 8, 9 -> {
                    val width = (CommonUtils.getScreenWidth(mContext) - CommonUtils.dip2px(
                        mContext,
                        108f
                    )) / 3
                    lp?.width = width
                    lp?.height = width
                    loadImage(imageView, mDatas, index, 0)
                }
                else -> {//
                    Toast.makeText(mContext, "数量不对，无法处理", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
}