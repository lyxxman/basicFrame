package com.frame.module.demo.activity.webview

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import com.frame.basic.base.ktx.onClick
import com.frame.basic.base.ktx.viewBindings
import com.frame.basic.base.mvvm.c.WebViewPlugin
import com.frame.basic.base.mvvm.c.vms
import com.frame.basic.base.mvvm.vm.BaseVM
import com.frame.basic.base.utils.ToastUtils
import com.frame.basic.common.demo.common.R
import com.frame.basic.common.demo.common.databinding.CommonDemoErrorContainerViewBinding
import com.frame.basic.common.demo.ui.CommonBaseActivity
import com.frame.basic.common.demo.ui.CommonTitleBar
import com.frame.module.demo.databinding.DemoActivityWebviewBinding
import javax.inject.Inject

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2022/7/27 13:49
 * @Version:
 */
class WebViewActivity : CommonBaseActivity<DemoActivityWebviewBinding, WebViewActivity.WebViewVM>(),
    WebViewPlugin {
    override fun DemoActivityWebviewBinding.initView() {

    }

    override fun DemoActivityWebviewBinding.initListener() {
        mBindingVM.url.observe(this@WebViewActivity) {
            webView.loadUrl(it)
        }
    }

    override fun title() = "WebViewPlugin插件Demo"
    override val mBindingVM: WebViewVM by vms()
    override val downloadType: Array<String> = arrayOf(".apk", ".rar")
    override val multipleWindow: ((url: String) -> Unit) = {
        startActivity(Intent(this, WebViewNewTabActivity::class.java).apply {
            putExtra("url", it)
        })
    }

    override fun getWebView() = mBinding.webView
    override fun goBackEnd() {
        finish()
    }

    override fun onReceivedTitle(title: String?) {
        (this.getTitleBar() as? CommonTitleBar)?.apply {
            titleText?.text = title ?: ""
        }
    }

    override fun getJavascriptInterface(): HashMap<String, Any>? = null

    override fun onProgressChanged(newProgress: Int) {
        mBinding.progressBar.progress = newProgress
        mBinding.progressBar.isVisible = newProgress != 100
    }

    override fun onPageStarted(url: String?) {
    }

    override fun onPageFinished(url: String?) {
        mBindingVM.loadSuccess()
    }

    override fun onReceivedError(errorCode: Int) {
        mBindingVM.loadError(errorCode, "网页加载失败")
    }

    override fun onReceivedDownload(url: String) {
        ToastUtils.showShort("开始下载文件")
    }

    override fun onShowFileChooser(
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: WebChromeClient.FileChooserParams?
    ): Boolean = true

    override fun back(): (() -> Boolean) = {
        !goBack()
    }

    override fun getErrorContainerView(error: Int, msg: String?): View {
        val vb = LayoutInflater.from(this)
            .inflate(R.layout.common_demo_error_container_view, null, false)
            .viewBindings<CommonDemoErrorContainerViewBinding>()
            .apply {
                message.text = "操作失败！错误码：${error}  错误信息：${msg}"
                message.onClick {
                    mBindingVM.loading()
                    reload()
                }
            }
        return vb.root
    }

    class WebViewVM @Inject constructor(handle: SavedStateHandle) : BaseVM(handle) {
        val url by savedStateLiveData<String>("url")
        override fun onRefresh(owner: LifecycleOwner) {
            url.postValue("https://www.baidu.com")
        }
    }
}