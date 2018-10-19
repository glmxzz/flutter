package com.ybx.flutter.plugin.apiplugin

import android.content.Context
import com.baidu.location.BDLocation
import com.baidu.location.BDLocationListener
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import java.util.*

/**
 * ø
 * Created by davidye on 17-3-26.
 */

class LbsManager private constructor() {

    private var locationClient: LocationClient? = null
    private var myListener: LocationListener? = null
    private val `object` = Any()

    private val notifyList = ArrayList<LocationNotify>()
    var curProvince: String? = null
        private set
    var latitude: String? = null
        private set
    var longitude: String? = null
        private set
    private var failedSum = 0
    private var isSingleReq = false

    /**
     * application 初始化即可。不需要多次调用。
     * @param context
     */
    fun init(context: Context) {
        locationClient = LocationClient(context)
        myListener = LocationListener()
        locationClient!!.registerLocationListener(myListener!!)    //注册监听函数


        val option = LocationClientOption()
        //option.setOpenGps(true);
        option.setCoorType("WGS84")//返回的定位结果是百度经纬度,默认值gcj02
        //option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
        option.disableCache(true)//禁止启用缓存定位
        option.setPriority(LocationClientOption.NetWorkFirst)
        locationClient!!.locOption = option
        locationClient!!.start()
    }

    fun addNotify(notify: LocationNotify) {
        synchronized(`object`) {
            if (curProvince != null
                    && latitude != null
                    && longitude != null) {
                notify.notify(curProvince, latitude, longitude)
            } else {
                notifyList.add(notify)
            }
        }
    }

    fun removeNotify(notify: LocationNotify) {
        synchronized(`object`) {
            if (!notifyList.isEmpty()) {
                notifyList.remove(notify)
            }

        }
    }

    fun deinit() {
        locationClient!!.unRegisterLocationListener(myListener!!)
        stop()
        locationClient = null
    }

    /**
     * 开始或重试定位。
     */
    private fun retry() {

        if (locationClient != null && locationClient!!.isStarted) {
            locationClient!!.requestLocation()
        }


    }


    @JvmOverloads
    fun start(isSingleReq: Boolean = false) {
        //每次启动都置为0，确保三次重试。
        failedSum = 0
        this.isSingleReq = isSingleReq
        retry()
    }

    private fun clear() {
        notifyList.clear()
    }

    private fun stop() {
        if (locationClient != null) {
            locationClient!!.stop()
        }
        clear()


    }


    fun callback(isSuccess: Boolean) {
        synchronized(`object`) {
            for (notify in notifyList) {
                // 正确的逻辑是失败也要回调notify
                notify?.notify(curProvince, latitude, longitude)
            }

            //成功才清空，否则不清空，因为有重试机制在。
            //单次请求，也清空。
            if (isSuccess || isSingleReq) {
                clear()
            }
        }
    }

    interface LocationNotify {
        fun notify(provice: String?, latitude: String?, longitude: String?)
    }

    inner class LocationListener : BDLocationListener {

        override fun onReceiveLocation(location: BDLocation) {
            doLocationResult(location)
        }

        override fun onConnectHotSpotMessage(s: String, i: Int) {}

        /**
         * 处理定位结果
         *
         * @param location
         */
        private fun doLocationResult(location: BDLocation?) {
            /**
             * 61 ： GPS定位结果
             * 65 ： 定位缓存的结果。
             * 66 ： 离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果
             * 161： 表示网络定位结果
             *
             * 62 ： 扫描整合定位依据失败。此时定位结果无效。
             * 63 ： 网络异常，没有成功向服务器发起请求。此时定位结果无效。
             * 67 ： 离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果
             * 68 ： 网络连接失败时，查找本地离线定位时对应的返回结果
             * 162~167： 服务端定位失败
             * 502：key参数错误
             * 505：key不存在或者非法
             * 601：key服务被开发者自己禁用
             * 602：key mcode不匹配
             * 501～700：key验证失败
             */
            var isResult = false
            if (location != null) {
                val locType = location.locType
                when (locType) {
                    BDLocation.TypeGpsLocation, BDLocation.TypeNetWorkLocation, BDLocation.TypeCacheLocation, BDLocation.TypeOffLineLocation -> {
                        curProvince = location.province
                        latitude = location.latitude.toString()
                        longitude = location.longitude.toString()

                        isResult = true
                    }

                    else -> {
                    }
                    //其他情况则是错误的

                }


            }

            callback(isResult)

            //非单次请求的话，会重试。
            if (!isSingleReq) {
                if (!isResult) {
                    failedSum++
                }

                //成功或者失败次数大于等于三次则停止定位, 否则再次去定位
                if (isResult || failedSum >= MAX_LBS_TIME) {
                    clear()
                } else {
                    retry()
                }
            } else {
                failedSum = 0
                isSingleReq = false
            }
        }
    }

    companion object {

        val instance = LbsManager()
        private val MAX_LBS_TIME = 3
    }

}
