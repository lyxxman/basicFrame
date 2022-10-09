package com.frame.module.demo.bean

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.frame.module.demo.activity.callparams.CallParamsActivity
import com.frame.module.demo.activity.dialog.DialogActivity
import com.frame.module.demo.activity.fragment.FragmentActivity
import com.frame.module.demo.activity.ipc.IpcActivity
import com.frame.module.demo.activity.list.CoordinatorActivity
import com.frame.module.demo.activity.list.MultiTypeRecyclerViewActivity
import com.frame.module.demo.activity.list.SingleTypeRecyclerViewActivity
import com.frame.module.demo.activity.maininteraction.MainInteractionActivity
import com.frame.module.demo.activity.mainlayout.MainLayoutActivity
import com.frame.module.demo.activity.modularization.ModularizationActivity
import com.frame.module.demo.activity.page.PageActivity
import com.frame.module.demo.activity.popu.PopWindowActivity
import com.frame.module.demo.activity.progress.ProgressActivity
import com.frame.module.demo.activity.refreshlayout.RefreshLayoutActivity
import com.frame.module.demo.activity.shareviewmodel.ShareViewModelsActivity
import com.frame.module.demo.activity.shareviewmodel.ShareViewModelsKeepActivity
import com.frame.module.demo.activity.tab.TabActivity
import com.frame.module.demo.activity.timer.TimerActivity
import com.frame.module.demo.activity.webview.WebViewActivity

/**
 * @Description:    菜单
 * @Author:         fanj
 * @CreateDate:     2021/11/11 13:49
 * @Version:        1.0.2
 */
enum class Menu(val desc:String, val cls: Class<out Activity>?) {
    MODULARIZATION_DEMO("组件化通讯Demo", ModularizationActivity::class.java),
    IPC_DEMO("多进程通讯Demo", IpcActivity::class.java),
    MAIN_LAYOUT_DEMO("布局Demo", MainLayoutActivity::class.java),
    MAIN_INTERACTION_DEMO("交互Demo", MainInteractionActivity::class.java),
    REFRESH_LAYOUT_PLUGIN_DEMO("RefreshLayoutPlugin插件Demo", RefreshLayoutActivity::class.java),
    RECYCLERVIEW_BASIC_PLUGIN_DEMO("RecyclerViewBasicPlugin插件Demo", SingleTypeRecyclerViewActivity::class.java),
    RECYCLERVIEW_MULTI_PLUGIN_DEMO("RecyclerViewMultiPlugin插件Demo", MultiTypeRecyclerViewActivity::class.java),
    COORDINATOR_PLUGIN_DEMO("CoordinatorPlugin插件Demo", CoordinatorActivity::class.java),
    TAB_PLUGIN_DEMO("TabPlugin插件Demo", TabActivity::class.java),
    WEB_VIEW_PLUGIN_DEMO("WebViewPlugin插件Demo", WebViewActivity::class.java),
    PAGING_CONTROL_DEMO("PagingControl控制器Demo", PageActivity::class.java),
    SHARE_VIEW_MODELS_DEMO("ViewModels共享Demo", ShareViewModelsActivity::class.java),
    SHARE_VIEW_MODELS_KEEP_DEMO("ViewModels共享Keep_Demo", ShareViewModelsKeepActivity::class.java),
    CALL_PARAMS_DEMO("界面传参Demo", CallParamsActivity::class.java),
    PROGRESS_DEMO("上传下载Demo", ProgressActivity::class.java),
    FRAGMENT_DEMO("Fragment相关Demo", FragmentActivity::class.java),
    DIALOG_DEMO("Dialog相关Demo", DialogActivity::class.java),
    POP_DEMO("PopWindow相关Demo", PopWindowActivity::class.java),
    TIMER_DEMO("TimerManager相关Demo", TimerActivity::class.java);

    fun open(context: Context){
        cls?.let {
            context.startActivity(Intent(context, it))
        }
    }
}