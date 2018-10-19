package com.ybx.flutter.plugin.apiplugin

import android.app.Activity
import android.content.Context
import com.haohan.android.common.api.config.AppConfig
import com.ybx.flutter.plugin.apiplugin.channel.ChannelUtils

import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import java.util.*

class ApiPlugin(val activity: Activity, val channel: MethodChannel) : MethodCallHandler, LbsManager.LocationNotify {

    val APIKEY = "api_key"
    val APP_NAME = "app_name"
    val APP_VER = "app_ver"
    val UDID = "udid"
    val NET = "net"
    val SP = "sp"
    val RES = "res"
    val GPS = "gps"
    val CHANNEL = "channel"
    val DINFO = "dinfo"
    val TS = "ts"
    val NONCE = "nonce"
    val ACCESS_TOKEN = "access_token"
    val XSIGN = "x-sign"

    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar): Unit {
            val channel = MethodChannel(registrar.messenger(), "api_plugin")
            channel.setMethodCallHandler(ApiPlugin(registrar.activity(), channel))
//            LbsManager.instance.init(registrar.activity())

        }
    }

    override fun onMethodCall(call: MethodCall, result: Result): Unit {
        when (call.method) {
            "getPlatformVersion" -> {
                result.success(getApiConfig(activity))
            }

            "getSignHash" -> {
                if (call.arguments is Map<*, *>) {
                    //D -> java  有序变成无序。。。
                    val map = call.arguments as Map<String, String>
                    val treeMap = TreeMap<String, String>()

                    map.filterValues { !it.isBlank() }
                            .forEach { t, u ->
                                treeMap[t] = u
                            }

                    result.success(AppConfig.getSignHash(activity, treeMap, ""))
                }
            }

            else -> {
                result.notImplemented()
            }
        }
    }


    override fun notify(provice: String?, latitude: String?, longitude: String?) {
        channel.invokeMethod("updateLocation", "$latitude $longitude")
    }


    fun getApiConfig(context: Context): Map<String, String> {
        return HashMap<String, String>().apply {
            put(APIKEY, AppConfig.getSignKey())
            put(APP_NAME, Constans.appName)
            put(APP_VER, ApiConfigUtils.getAppVersionName(context))
            put(NET, ApiConfigUtils.getNetWorkType(context))
            put(SP, ApiConfigUtils.getNetworkCarrier(context))
            put(RES, ApiConfigUtils.getScreenRes(context))
            put(UDID, ApiConfigUtils.getUdid(context))
            put(CHANNEL, ChannelUtils.getChannel(context))
            put(DINFO, ApiConfigUtils.getDInfo())
            put(TS, System.currentTimeMillis().toString())
            put(NONCE, ApiConfigUtils.getSixRandom())

        }

    }


}
