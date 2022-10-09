package com.frame.module.demo.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.frame.basic.base.mvvm.c.IndicatorPlugin
import com.frame.basic.base.mvvm.c.vms
import com.frame.basic.base.mvvm.vm.BaseVM
import com.frame.basic.common.demo.ui.CommonBaseFragment
import com.frame.module.demo.databinding.DemoActivityFragmentBinding
import com.frame.module.demo.fragment.callparams.CallParamsFragment
import com.frame.module.demo.fragment.list.CoordinatorFragment
import com.frame.module.demo.fragment.list.MultiTypeRecyclerViewFragment
import com.frame.module.demo.fragment.list.SingleTypeRecyclerViewFragment
import com.frame.module.demo.fragment.maininteraction.MainInteractionFragment
import com.frame.module.demo.fragment.mainlayout.MainLayoutFragment
import com.frame.module.demo.fragment.page.PageFragment
import com.frame.module.demo.fragment.refreshlayout.RefreshLayoutFragment
import com.frame.module.demo.fragment.shareviewmodel.ShareViewModelsFragment
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2022/7/4 14:53
 * @Version:
 */
open class InnerFragment : CommonBaseFragment<DemoActivityFragmentBinding, InnerFragmentVM>(),
    IndicatorPlugin<String> {
    override val mBindingVM: InnerFragmentVM by vms()
    override fun DemoActivityFragmentBinding.initView() {}
    override fun DemoActivityFragmentBinding.initListener() {}

    override fun getPagerTitleView(context: Context, index: Int, title: String) =
        CommonPagerTitleView(context).apply {
            val textView = AppCompatTextView(context).apply {
                text = title
            }
            setContentView(textView)
            onPagerTitleChangeListener = object : CommonPagerTitleView.OnPagerTitleChangeListener {
                override fun onSelected(index: Int, totalCount: Int) {
                    textView.typeface = Typeface.DEFAULT_BOLD
                    textView.setTextColor(Color.RED)
                }

                override fun onDeselected(index: Int, totalCount: Int) {
                    textView.typeface = Typeface.DEFAULT
                    textView.setTextColor(Color.GRAY)
                }

                override fun onLeave(
                    index: Int,
                    totalCount: Int,
                    leavePercent: Float,
                    leftToRight: Boolean
                ) {
                }

                override fun onEnter(
                    index: Int,
                    totalCount: Int,
                    enterPercent: Float,
                    leftToRight: Boolean
                ) {
                }

            }
        }

    override fun getMagicIndicator() = mBinding.tabLayout
    override fun getViewPager() = mBinding.viewPager
    override fun getIndicatorFragments(): ArrayList<Fragment> {
        return ArrayList<Fragment>().apply {
            childFragmentManager.fragments.let { history ->
                add(history.find { it is MainLayoutFragment } ?: MainLayoutFragment())
                add(history.find { it is MainInteractionFragment } ?: MainInteractionFragment())
                add(history.find { it is RefreshLayoutFragment } ?: RefreshLayoutFragment())
                add(history.find { it is SingleTypeRecyclerViewFragment }
                    ?: SingleTypeRecyclerViewFragment())
                add(history.find { it is MultiTypeRecyclerViewFragment }
                    ?: MultiTypeRecyclerViewFragment())
                add(history.find { it is CoordinatorFragment } ?: CoordinatorFragment())
                add(history.find { it is PageFragment } ?: PageFragment())
                add(history.find { it is ShareViewModelsFragment } ?: ShareViewModelsFragment())
                add(history.find { it is CallParamsFragment }
                    ?: CallParamsFragment().putExtra("params", "我是传输的参数，看到我了吗"))
            }
        }
    }

    override fun getIndicatorTitles(): MutableLiveData<ArrayList<String>> =
        mBindingVM.indicatorTitles

    override fun getCurrentPosition(): MutableLiveData<Int> = mBindingVM.currentPos
    override fun isAttachToViewPager() = true
}

class InnerFragmentVM(savedStateHandle: SavedStateHandle) : BaseVM(savedStateHandle) {
    val currentPos by savedStateLiveData("currentPos", 0)
    val indicatorTitles by savedStateLiveData(
        "indicatorTitles", arrayListOf(
            "布局Demo",
            "交互Demo",
            "RefreshLayoutPlugin插件Demo",
            "RecyclerViewBasicPlugin插件Demo",
            "RecyclerViewMultiPlugin插件Demo",
            "CoordinatorPlugin插件Demo",
            "PagingControl控制器Demo",
            "ViewModels共享Demo",
            "界面传参Demo"
        )
    )

    override fun onRefresh(owner: LifecycleOwner) {}

    override fun autoOnRefresh() = false
}