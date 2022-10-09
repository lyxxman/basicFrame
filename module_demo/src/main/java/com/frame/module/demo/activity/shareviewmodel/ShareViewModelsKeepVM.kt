package com.frame.module.demo.activity.shareviewmodel

import com.frame.basic.base.mvvm.c.localLiveData
import com.frame.basic.base.mvvm.vm.CoreVM

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/11/12 17:13
 * @Version:        1.0.2
 */
class ShareViewModelsKeepVM : CoreVM() {
    val data by localLiveData("data", 1000)
}