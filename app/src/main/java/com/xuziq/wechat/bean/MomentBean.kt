package com.xuziq.wechat.bean

import com.google.gson.annotations.SerializedName
import org.w3c.dom.Comment

/**
 *
 * @Description:     java类作用描述
 * @Author:         xuziq
 * @CreateDate:     2020/10/17 0:33
 * @UpdateDate:     2020/10/17 0:33
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
data class MomentBean(
    @SerializedName("content")
    var content:String,
    @SerializedName("images")
    var images:ArrayList<ImageList>,
    @SerializedName("sender")
    var sender:Sender,
    @SerializedName("comments")
    var comments:ArrayList<Comments>,
    @SerializedName("error")
    var error:String
)data class ImageList(
    @SerializedName("url")
    var url:String
)data class Sender(
    @SerializedName("username")
    var username:String,
    @SerializedName("nick")
    var nick:String,
    @SerializedName("avatar")
    var avatar:String
)data class Comments(
    @SerializedName("content")
    var content: String,
    @SerializedName("sender")
    var sender: Sender
)