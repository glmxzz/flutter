package com.ybx.flutter.plugin.apiplugin.channel

import org.json.JSONException
import org.json.JSONObject

import java.io.File
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.HashMap

class ChannelReader private constructor() {
    companion object {
        val CHANNEL_KEY = "channel"

        /**
         * easy api for get channel & extra info.<br></br>
         *
         * @param apkFile apk file
         * @return null if not found
         */
        operator fun get(apkFile: File): ChannelInfo? {
            val result = getMap(apkFile) ?: return null
            val channel = result[CHANNEL_KEY]
            result.remove(CHANNEL_KEY)
            return ChannelInfo(channel!!, result)
        }

        /**
         * get channel & extra info by map, use [PayloadReader.CHANNEL_KEY][ChannelReader.CHANNEL_KEY] get channel
         *
         * @param apkFile apk file
         * @return null if not found
         */
        fun getMap(apkFile: File): MutableMap<String, String>? {
            try {
                val rawString = getRaw(apkFile) ?: return null
                val jsonObject = JSONObject(rawString)
                val keys = jsonObject.keys()
                val result = HashMap<String, String>()
                while (keys.hasNext()) {
                    val key = keys.next().toString()
                    result[key] = jsonObject.getString(key)
                }
                return result
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return null
        }

        /**
         * get raw string from channel id
         *
         * @param apkFile apk file
         * @return null if not found
         */
        fun getRaw(apkFile: File): String? {
            val bytes = PayloadReader.get(apkFile, ApkUtil.APK_CHANNEL_BLOCK_ID) ?: return null
            try {
                return String(bytes, Charset.forName(ApkUtil.DEFAULT_CHARSET))
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }

            return null
        }
    }
}
