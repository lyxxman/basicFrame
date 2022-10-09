package com.frame.module.demo.activity.modularization

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import com.frame.basic.base.ktx.onClick
import com.frame.basic.base.mvvm.c.getServiceProvider
import com.frame.basic.base.mvvm.c.toServiceBean
import com.frame.basic.base.mvvm.c.vms
import com.frame.basic.base.mvvm.vm.BaseVM
import com.frame.basic.common.demo.ui.CommonBaseActivity
import com.frame.basic.provider.service.DemoService
import com.frame.module.demo.bean.UserBean
import com.frame.module.demo.databinding.DemoActivityModularizationBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/11/17 12:46
 * @Version:        1.0.2
 */
@AndroidEntryPoint
class ModularizationActivity :
    CommonBaseActivity<DemoActivityModularizationBinding, ModularizationVM>() {
    override val mBindingVM: ModularizationVM by vms()

    override fun DemoActivityModularizationBinding.initView() {
    }

    override fun DemoActivityModularizationBinding.initListener() {
        useService.onClick {
            getServiceProvider<DemoService>()?.toastSomething("感谢您访问DemoService...")
        }
        modelSay.onClick {
            //可以看到UserBean并没有下沉到common层，双方通过json格式进行了互转
            //值得注意的是服务方和调用方使用了不同的class，调用方只需适配自己需要的字段即可。同时服务方须在service实现类里面标注复杂类型的ServiceBean文档注解，方便调用方适配
            val callBean = UserBean("服务方")
            val result = getServiceProvider<DemoService>()?.speakSomething(callBean, "感谢您访问DemoService...")
            result?.let {
                mBindingVM.say.value = it.toServiceBean<UserBean>().name
            }
        }
        mBindingVM.say.observe(this@ModularizationActivity){
            modelSay.text = it
        }
    }

    override fun title() = "组件化通讯Demo"
}
class ModularizationVM(handle: SavedStateHandle): BaseVM(handle){
    val say by savedStateLiveData<String>("say")
    override fun onRefresh(owner: LifecycleOwner) {
    }
    override fun autoOnRefresh() = false
}