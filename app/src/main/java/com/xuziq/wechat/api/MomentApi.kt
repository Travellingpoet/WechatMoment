package com.xuziq.wechat.api

import com.xuziq.wechat.bean.MomentBean
import com.xuziq.wechat.bean.ProfileBean
import com.xuziq.wechat.config.UrlConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

/**
 *
 * @Description:     java类作用描述
 * @Author:         xuziq
 * @CreateDate:     2020/10/17 12:33
 * @UpdateDate:     2020/10/17 12:33
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
interface MomentApi {
    @GET(UrlConfig.BASE_INFO)
   fun getUser(): Call<ProfileBean>

    @GET(UrlConfig.MOMENT_LIST)
    fun getList():Call<List<MomentBean>>


}