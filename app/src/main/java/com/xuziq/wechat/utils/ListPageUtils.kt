package com.xuziq.wechat.utils

import java.util.*
import kotlin.collections.ArrayList


/**
 *
 * @Description:     java类作用描述
 * @Author:         xuziq
 * @CreateDate:     2020/10/18 16:47
 * @UpdateDate:     2020/10/18 16:47
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class ListPageUtils <T> {
    private var pageSize = 0
    private var pageCount = 0
    private var data = arrayListOf<T>()

    constructor( data: ArrayList<T>,pageSize: Int) {
        if (data == null || data.isEmpty()) {
            throw IllegalArgumentException("data must be not empty!")
        }
        this.pageSize = pageSize
        this.data = data
        this.pageCount = data.size / pageSize
        if (data.size % pageSize != 0) {
            pageCount++
        }
    }

    fun getPageList(pageNum:Int):List<T>{
        val fromIndex = (pageNum - 1) * pageSize
        if (fromIndex >= data.size){
            return Collections.emptyList()
        }

        var toIndex = pageNum * pageSize
        if (toIndex >= data.size){
            toIndex = data.size
        }
        return data.subList(fromIndex,toIndex)
    }
}
