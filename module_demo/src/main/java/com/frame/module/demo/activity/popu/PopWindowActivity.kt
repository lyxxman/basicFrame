package com.frame.module.demo.activity.popu

import com.frame.basic.base.ktx.dp
import com.frame.basic.base.ktx.onClick
import com.frame.basic.base.mvvm.c.vms
import com.frame.basic.base.mvvm.v.TargetGravity
import com.frame.basic.base.mvvm.vm.EmptyVM
import com.frame.basic.common.demo.ui.CommonBaseActivity
import com.frame.module.demo.databinding.DemoActivityPopWindowBinding
import com.frame.module.demo.popu.pop.PopWindowDialog
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/11/29 15:35
 * @Version:        1.0.2
 */
@AndroidEntryPoint
class PopWindowActivity: CommonBaseActivity<DemoActivityPopWindowBinding, EmptyVM>() {
    override val mBindingVM: EmptyVM by vms()

    override fun DemoActivityPopWindowBinding.initView() {
    }

    override fun DemoActivityPopWindowBinding.initListener() {
        topLeft.onClick {
            PopWindowDialog().showAtLocation(supportFragmentManager, it, TargetGravity.TOP_LEFT)
        }
        topRight.onClick {
            PopWindowDialog().showAtLocation(supportFragmentManager, it, TargetGravity.TOP_RIGHT)
        }
        bottomLeft.onClick {
            PopWindowDialog().showAtLocation(supportFragmentManager, it, TargetGravity.BOTTOM_LEFT)
        }
        bottomRight.onClick {
            PopWindowDialog().showAtLocation(supportFragmentManager, it, TargetGravity.BOTTOM_RIGHT)
        }
        fitTarget.onClick {
            PopWindowDialog().showAtLocation(supportFragmentManager, it, TargetGravity.TOP)
        }
        verticalMargin.onClick {
            PopWindowDialog().showAtLocation(supportFragmentManager, it, TargetGravity.TOP_LEFT, verticalMargin = 10.dp)
        }
        horizontalMargin.onClick {
            PopWindowDialog().showAtLocation(supportFragmentManager, it, TargetGravity.BOTTOM_RIGHT, horizontalMargin = 10.dp)
        }
    }
    override fun title() = "PopWindow相关Demo"
}