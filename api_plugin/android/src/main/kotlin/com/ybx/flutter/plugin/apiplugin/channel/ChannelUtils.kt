package com.ybx.flutter.plugin.apiplugin.channel

import android.content.Context
import android.text.TextUtils


import java.io.File

/**
 * Created by davidye on 2017/4/7.
 */

object ChannelUtils {

    var OFFICIAL_CHANNEL = "MG_CA001"//官方渠道

    fun getChannel(context: Context): String {

        var channelInfo: ChannelInfo? = null
        val file = File(context.packageResourcePath)
        if (file != null && file.exists()) {
            channelInfo = ChannelReader.get(file)
        }

        if (channelInfo != null && !TextUtils.isEmpty(channelInfo.channel)) {
            return channelInfo.channel
        }

        return OFFICIAL_CHANNEL
    }
}
