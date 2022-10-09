package com.frame.module.demo.activity.progress

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import com.blankj.utilcode.util.ToastUtils
import com.frame.basic.base.BaseApplication
import com.frame.basic.base.mvvm.vm.BaseVM
import com.frame.basic.base.mvvm.vm.VMCall
import com.frame.basic.base.utils.DownLoadSpeedUtils
import com.frame.basic.base.utils.DownloadManager
import com.frame.basic.base.utils.DownloadTask
import com.frame.module.demo.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import java.io.File
import javax.inject.Inject

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/11/24 9:01
 * @Version:        1.0.2
 */
@HiltViewModel
class ProgressVM @Inject constructor(
    handle: SavedStateHandle,
    private val homeRepository: HomeRepository
) : BaseVM(handle) {
    val progressData by savedStateLiveData<String>("progressData")
    val downloadSpeed by savedStateLiveData<String>("downloadSpeedData")
    override fun onRefresh(owner: LifecycleOwner) {
    }

    override fun autoOnRefresh() = false
    private var uploadJob: Job? = null
    fun upload(file: File) {
        uploadJob = launch(object : VMCall {
            override val execute: suspend CoroutineScope.() -> Unit = {
                homeRepository.uploadFiles(file) { byte: Long, totalLength: Long ->
                    progressData.postValue("$byte/$totalLength")
                }
                ToastUtils.showShort("上传成功：${file.name}")
            }

            override fun onError(code: Int, error: String?, e: Throwable) {
                ToastUtils.showShort("上传失败：$error")
            }

        })
    }

    private val downloadManager by lazy { DownloadManager.get() }
    fun download(owner: LifecycleOwner, url: String) {
        val downloadTask = downloadManager.createTask(url) { builder ->
            builder.setTargetDict(BaseApplication.application.cacheDir) //设置下载目录，必须设置
            //builder.allowSafeSpeedModel() //允许网速保护限制，默认允许
            //builder.ignoreSafeSpeedModel(Long.MAX_VALUE) //禁止网速保护模式限制，并允许最大速率
            //builder.speedLimit(1024*1024) //限速1兆
            //builder.autoReconnect(true) //允许断网恢复后自动重连，默认允许
            builder.networkType(DownloadTask.NetworkType.ONLY_WIFI) //仅WIFI网络下载， 默认全部
            builder.attachTo(owner) //绑定到生命周期，随生命周期自动终止
        }
        //监听下载状态
        downloadTask.state.observe(owner) {
            when (it) {
                DownloadTask.State.SUCCESS -> {
                    ToastUtils.showShort("下载成功：${downloadTask.getTargetFile().name}")
                }
                DownloadTask.State.ERROR -> {
                    ToastUtils.showShort("下载失败")
                }
                DownloadTask.State.CANCEL -> {
                    ToastUtils.showShort("下载取消")
                }
                DownloadTask.State.RECONNECTION -> {
                    ToastUtils.showShort("重连中")
                }
                DownloadTask.State.DOWNLOADING -> {
                    ToastUtils.showShort("下载中")
                }
                DownloadTask.State.NO_SUPPORT_NETWORK_TYPE -> {
                    ToastUtils.showShort("不符合网络条件")
                }
                DownloadTask.State.WAIT -> {
                    ToastUtils.showShort("排队等待中")
                }
            }
        }
        //监听下载进度
        downloadTask.progress.observe(owner) {
            progressData.postValue("${it.current}/${it.total}")
            downloadSpeed.postValue(DownLoadSpeedUtils.speedFormat(it.speed))
        }
        //downloadTask.restoreSpeed() //解除速度限制
        //downloadTask.allowSafeSpeedModel() //允许网速保护限制，默认允许
        //downloadTask.ignoreSafeSpeedModel(Long.MAX_VALUE) //禁止网速保护模式限制，并允许最大速率
        //downloadTask.speedLimit(1024*1024) //限速1兆
        downloadManager.download(downloadTask)
    }

    fun cancelUpload() {
        uploadJob?.cancel()
    }

    fun cancelDownload(url: String) {
        downloadManager.cancel(url)
    }

    fun limitSpeed() {
        downloadManager.startSafeSpeedModel(1024 * 1024)
//        downloadJob?.speedLimit(1024*1024)
    }

    fun restoreSpeed() {
        downloadManager.exitSafeSpeedModel()
        //downloadJob?.speedLimit(Long.MAX_VALUE)
    }
}