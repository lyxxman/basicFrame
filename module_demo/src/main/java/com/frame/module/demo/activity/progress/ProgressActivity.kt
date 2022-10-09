package com.frame.module.demo.activity.progress

import com.frame.basic.base.mvvm.c.vms
import com.frame.basic.base.BaseApplication
import com.frame.basic.base.ktx.onClick
import com.frame.basic.base.ktx.toUrlFileName
import com.frame.basic.common.demo.ui.CommonBaseActivity
import com.frame.module.demo.databinding.DemoActivityProgressBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/11/24 9:01
 * @Version:        1.0.2
 */
@AndroidEntryPoint
class ProgressActivity : CommonBaseActivity<DemoActivityProgressBinding, ProgressVM>() {
    override val mBindingVM: ProgressVM by vms()

    override fun title() = "上传下载Demo"
    override fun DemoActivityProgressBinding.initView() {
        mBindingVM.progressData.observe(this@ProgressActivity){
            tvProgress.text = it
        }
        mBindingVM.downloadSpeed.observe(this@ProgressActivity){
            tvSpeed.text = it
        }
    }
    private val url = "https://cdn.yuedui.love/apk/app-release.apk"
    private val file = File(BaseApplication.application.cacheDir, url.toUrlFileName())
    override fun DemoActivityProgressBinding.initListener() {
        btnUpload.onClick {
            mBindingVM.upload(file)
        }
        btnCancelUpload.onClick {
            mBindingVM.cancelUpload()
        }
        btnDownload.onClick {
            mBindingVM.download(this@ProgressActivity,url)
        }
        btnCancelDownload.onClick {
            mBindingVM.cancelDownload(url)
        }
        btnCancelLimit.onClick {
            mBindingVM.limitSpeed()
        }
        btnCancelLimitRestore.onClick {
            mBindingVM.restoreSpeed()
        }
    }
}