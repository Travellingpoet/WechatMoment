package com.xuziq.wechat.bean

import com.google.gson.annotations.SerializedName

/**
 *
 * @Description:     java类作用描述
 * @Author:         xuziq
 * @CreateDate:     2020/10/17 12:29
 * @UpdateDate:     2020/10/17 12:29
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
data class ProfileBean(
    @SerializedName("profile-image")
    var profileImage:String,
    @SerializedName("avatar")
    var avatar:String,
    @SerializedName("nick")
    var nick:String,
    @SerializedName("username")
    var username:String
)