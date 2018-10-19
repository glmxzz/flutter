package com.haohan.android.common.api.config

import android.content.Context


/**
 * 签名配调用入口
 * davidye
 */
object AppConfig {
    init {
        try {
            System.loadLibrary("appcore")
        } catch (e: Throwable) {
            e.printStackTrace()
        }

    }

    external fun getSignKey(): String
    external fun getSignHash(ctx: Context, paraMap: Map<String, String>, usertoken: String): String
    external fun encodeString(data: String): String
}
