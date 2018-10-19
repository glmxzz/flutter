package com.ybx.flutter.plugin.apiplugin

import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import java.util.*

object ApiConfigUtils {

    private const val NETWORT_2G = "2G"
    private const val NETWORT_3G = "3G"
    private const val NETWORT_4G = "4G"
    private const val NETWORT_WIFI = "WIFI"
    private const val SHARE_P_NAME = "API_CONFIG"
    private const val UDID_KEY = "UDID_KEY"


    /**
     * 获取应用版本号
     *
     * @return
     */
    fun getAppVersionName(context: Context): String {
        var version = ""
        try {
            val packageManager = context.packageManager
            var packInfo = packageManager.getPackageInfo(context.packageName, 0)
            version = packInfo.versionName

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return version
    }


    /**
     * 检查当前网络是2G还是3G,或者wifi
     */
    fun getNetWorkType(context: Context): String {

        var netCode = NETWORT_WIFI
        val mWifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        var wifiInfo = mWifiManager.connectionInfo


        val ipAddress = wifiInfo.ipAddress
        if (mWifiManager.isWifiEnabled && ipAddress != 0) {
            netCode = NETWORT_WIFI
        } else {
            val telMgr = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (telMgr.dataState == TelephonyManager.DATA_CONNECTED) {
                val type = telMgr.networkType
                netCode = getNetworkType(type)
            }
        }

        return netCode
    }


    private fun getNetworkType(type: Int): String {
        var netCode = NETWORT_WIFI

        when (type) {
            TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN, 16    // GSM, but hide
            -> netCode = NETWORT_2G
            TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> netCode = NETWORT_3G
            TelephonyManager.NETWORK_TYPE_LTE -> netCode = NETWORT_4G
            else -> if (type > 0) {
                netCode = NETWORT_4G
            }
        }

        return netCode
    }

    /**
     * 取得网络运营商
     *
     * @return 网络类型
     */
    fun getNetworkCarrier(context: Context): String {
        return getSimOperator(context)
    }

    fun getScreenRes(context: Context): String {
        return context.resources.displayMetrics.widthPixels.toString() + "*" + context.resources.displayMetrics.heightPixels
    }


    fun getUdid(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(SHARE_P_NAME, Context.MODE_APPEND)
            var udid = sharedPreferences.getString(UDID_KEY, "")
            if (udid.isBlank()) {
                udid = getDeviceUuid(context).toString()
                if (udid.isBlank()) {
                    udid = UUID.randomUUID().toString()
                }
                sharedPreferences.edit().putString(UDID_KEY, udid).apply()
            }

        return udid
    }

    private fun getDeviceUuid(context: Context): UUID {
        var uuid: UUID
        val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        uuid = try {
            if ("9774d56d682e549c" != androidId) {
                UUID.nameUUIDFromBytes(androidId.toByteArray(charset("utf8")))
            } else {
                val deviceId = (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
                if (deviceId != null) UUID.nameUUIDFromBytes(deviceId.toByteArray(charset("utf8"))) else UUID.randomUUID()
            }
        } catch (e: Exception) {
            UUID.randomUUID()
        }

        return uuid
    }

    /**
     * 获取SIM卡提供的移动国家码和移动网络码.5或6位的十进制数字.
     *
     * @param context
     * @return
     */
    private fun getSimOperator(context: Context): String {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        val op = tm.simOperator
        if (op != null && op.length >= 5) {
            return op.substring(0, 5)
        }

        return "-1"
    }


    fun getDInfo(): String {
        return getDeviceModel() + ":" + "android" + ":" + getAndroidVersion()
    }

    private fun getDeviceModel(): String {
        return Build.MODEL
    }

    private fun getAndroidVersion(): Int {
        return Build.VERSION.SDK_INT
    }


    /**
     * 返回6位随机码
     * @return
     */
    fun getSixRandom(): String {
        val random = Random()
        val result = StringBuilder()
        for (i in 0..5) {
            result.append(random.nextInt(10))
        }
        return result.toString()
    }
}