package com.xuziq.wechat.adpater

import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xuziq.wechat.R
import com.xuziq.wechat.bean.Comments

/**
 *
 * @Description:     java类作用描述
 * @Author:         xuziq
 * @CreateDate:     2020/10/18 19:38
 * @UpdateDate:     2020/10/18 19:38
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class CommentAdapter:BaseQuickAdapter<Comments,BaseViewHolder>(R.layout.item_list_comment){
    private var colorSpannableStringCommenter : SpannableString?= null

    override fun convert(helper: BaseViewHolder, item: Comments?) {
        helper.setText(R.id.tvReplyHead,spellString(item?.sender?.nick,item?.content))
    }

    /**
     * 拼接不同颜色的字符串
     * */
    private fun spellString(userName: String?,sayText:String?): SpannableStringBuilder {
        val ssb = SpannableStringBuilder()
        colorSpannableStringCommenter = SpannableString("$userName")


        val colorSpanBlue = ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.textBlue))

        colorSpannableStringCommenter?.length?.let { colorSpannableStringCommenter?.setSpan(colorSpanBlue,0, it, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) }

        ssb.append(colorSpannableStringCommenter).append(":").append(sayText?:"")

        return ssb
    }
}