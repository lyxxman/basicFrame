package com.frame.module.demo.activity.tab

import com.frame.module.demo.fragment.InnerFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Menu2Fragment : InnerFragment(){
    override fun isAttachToViewPager() = false
}
