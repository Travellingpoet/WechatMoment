package com.xuziq.wechat.http

import com.xuziq.wechat.config.UrlConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 *
 * @Description:     java类作用描述
 * @Author:         xuziq
 * @CreateDate:     2020/10/17 13:30
 * @UpdateDate:     2020/10/17 13:30
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class RetrofitManager {
    val CONNECT_TIME_OUT = 10000L //毫秒

    init {
        createRetrofit()
    }
    companion object{
        private var mRetrofit: Retrofit? = null
        private var retrofitManager: RetrofitManager? = null
         fun getInstance(): RetrofitManager? {
            if (retrofitManager == null) {
                synchronized(RetrofitManager::class.java) {
                    if (retrofitManager == null) {
                        retrofitManager = RetrofitManager()
                    }
                }
            }
            return retrofitManager
        }


    }
    private fun createRetrofit(){
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIME_OUT,TimeUnit.MILLISECONDS)
            .build()
        mRetrofit = Retrofit.Builder()
            .baseUrl(UrlConfig.Base_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    fun getRetrofit():Retrofit?{
        return mRetrofit
    }




}