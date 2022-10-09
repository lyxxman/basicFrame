package com.frame.basic.base.utils

import android.net.TrafficStats


/**
 * @Description:    下载速度检测工具
 * @Author:         fanj
 * @CreateDate:     2022/6/30 9:26
 * @Version:
 */
object DownLoadSpeedUtils {
    private var lastTotalRxBytes: Long = 0
    private var lastTimeStamp: Long = 0

    /**
     * 获取网络下载速度
     *
     * @param uid
     * @return b/s
     */
    fun getNetSpeed(uid: Int): Long {
        val nowTotalRxBytes = getTotalRxBytes(uid)
        val nowTimeStamp = System.currentTimeMillis()
        if (nowTimeStamp == lastTimeStamp){
            return 0
        }
        val speed = (nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp) //毫秒转换
        lastTimeStamp = nowTimeStamp
        lastTotalRxBytes = nowTotalRxBytes
        var result = 0L
        try {
            result = speed
        } catch (e: Exception) {
        }
        return result *1024
    }

    private fun getTotalRxBytes(uid: Int): Long {
        //转为KB
        return if (TrafficStats.getUidRxBytes(uid) == TrafficStats.UNSUPPORTED.toLong()) 0L else TrafficStats.getTotalRxBytes() / 1024
    }

    /**
     * 下载速度字符串格式化
     *
     * @param speed bit/s
     * @return
     */
    fun speedFormat(speed: Long): String {
        val kSpeed = speed/1024
        val result: String = if (kSpeed > 1024) {
            val partA = kSpeed / 1024
            val partB = (kSpeed - partA * 1024) / 100
            partA.toString() + "." + partB + "m/s"
        } else {
            kSpeed.toString() + "kb/s"
        }
        return result
    }
}