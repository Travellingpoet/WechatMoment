package com.xuziq.wechat.utils

import android.content.Context
import android.content.res.Resources

/**
 *
 * @Description:     java类作用描述
 * @Author:         xuziq
 * @CreateDate:     2020/10/18 14:09
 * @UpdateDate:     2020/10/18 14:09
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class CommonUtils{
    companion object {
        /**
         * 获得资源
         */
        fun getResources(context: Context): Resources {
            return context.applicationContext.resources
        }

        /**
         * 获得屏幕的宽度
         *
         * @return
         */
        fun getScreenWidth(context: Context): Int {
            return getResources(context.applicationContext).displayMetrics.widthPixels
        }

        /**
         * dip转pix
         *
         * @param dpValue
         * @return
         */
        fun dip2px(context: Context, dpValue: Float): Int {
            val scale: Float = getResources(context.applicationContext)
                    .displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }
    }
}