package com.frame.module.demo.fragment.shareviewmodel

import android.content.Intent
import com.frame.basic.base.ktx.onClick
import com.frame.basic.base.mvvm.c.getViewModel
import com.frame.basic.base.mvvm.c.vms
import com.frame.basic.common.demo.ui.CommonBaseFragment
import com.frame.module.demo.activity.shareviewmodel.ShareViewModelsSecondActivity
import com.frame.module.demo.activity.shareviewmodel.ShareViewModelsVM
import com.frame.module.demo.databinding.DemoActivityShareViewModelsBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/11/15 10:22
 * @Version:        1.0.2
 */
@AndroidEntryPoint
class ShareViewModelsFragment : CommonBaseFragment<DemoActivityShareViewModelsBinding, ShareViewModelsVM>() {
    override val mBindingVM: ShareViewModelsVM by vms()

    override fun DemoActivityShareViewModelsBinding.initView() {

    }

    override fun DemoActivityShareViewModelsBinding.initListener() {
        mBindingVM.data.observe(viewLifecycleOwner) {
            text.text = "您输入了$it"
        }
        text.onClick {
            startActivity(Intent(requireContext(), ShareViewModelsSecondActivity::class.java))
        }
        btnUpdate.onClick {
            getViewModel<ShareViewModelsVM>()?.data?.postValue(10023)
        }
    }
    override fun isAttachToViewPager() = true
}