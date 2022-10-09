package com.frame.module.demo.activity.timer

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.frame.basic.base.ktx.*
import com.frame.basic.base.mvvm.c.RecyclerViewBasicPlugin
import com.frame.basic.base.mvvm.c.vms
import com.frame.basic.base.mvvm.vm.BaseVM
import com.frame.basic.base.utils.DateUtils
import com.frame.basic.base.utils.ToastUtils
import com.frame.basic.common.demo.ui.CommonBaseActivity
import com.frame.module.demo.R
import com.frame.module.demo.databinding.DemoActivityTimerItemBinding
import com.frame.module.demo.databinding.DemoRecyclerViewBinding
import kotlinx.coroutines.Dispatchers
import java.util.*

class TimerActivity : CommonBaseActivity<DemoRecyclerViewBinding, TimerVM>(),
    RecyclerViewBasicPlugin<TimerType, DemoActivityTimerItemBinding> {
    override fun DemoRecyclerViewBinding.initView() {
    }

    override fun DemoRecyclerViewBinding.initListener() {
    }

    override val mBindingVM: TimerVM by vms()
    override fun bindAdapterListener(adapter: BaseQuickAdapter<TimerType, BaseDataBindingHolder<DemoActivityTimerItemBinding>>) {
        adapter.setOnItemClickListener { _, _, position ->
            getMutableLiveData().value?.get(position)?.let {
                mBindingVM.executeTimer(this, it)
            }
        }
    }

    override fun getMutableLiveData(): MutableLiveData<MutableList<TimerType>> =
        mBindingVM.timerTypes

    override fun getItemId() = R.layout.demo_activity_timer_item
    override fun getRecyclerView(): RecyclerView = mBinding.recyclerView

    override fun convert(
        holder: BaseDataBindingHolder<DemoActivityTimerItemBinding>,
        item: TimerType
    ) {
        holder.dataBinding?.title?.text = item.desc
    }

    override fun title() = "TimerManager相关Demo"
}

class TimerVM(savedStateHandle: SavedStateHandle) : BaseVM(savedStateHandle) {
    val timerTypes by savedStateLiveData("timerTypes", TimerType.values().toMutableList())
    override fun onRefresh(owner: LifecycleOwner) {}
    override fun autoOnRefresh() = false
    fun executeTimer(owner: LifecycleOwner, timerType: TimerType) {
        when (timerType) {
            TimerType.DELAY_TASK -> {
                ToastUtils.showShort("5秒后执行任务")
                delayTask(5, dispatcher = Dispatchers.Main) {
                    ToastUtils.showShort("${TimerType.DELAY_TASK.desc}已执行")
                }.attachTo(owner)
            }
            TimerType.DATE_TIME_TASK -> {
                val dataTime = nowTime() + 60000
                val dataTimeFormat = DateUtils.getStringByFormat(dataTime, "yyyy-MM-dd HH:mm")
                ToastUtils.showShort("${dataTimeFormat}执行任务")
                dateTimeTask(Date(dataTime), dispatcher = Dispatchers.Main) {
                    ToastUtils.showShort("${TimerType.DATE_TIME_TASK.desc}已执行")
                }.attachTo(owner)
            }
            TimerType.INTERVAL_TASK -> {
                ToastUtils.showShort("每5秒执行1次任务， 执行3次")
                intervalTask(5, maxExecutes = 3, dispatcher = Dispatchers.Main) {
                    ToastUtils.showShort("${TimerType.INTERVAL_TASK.desc}已执行${it}次")
                }.attachTo(owner)
            }
            TimerType.DAILY_TASK -> {
                val dataTimeFormat = DateUtils.getStringByFormat(nowTime(), "HH:mm").split(":")
                val hour = dataTimeFormat[0].toInt()
                val minute = dataTimeFormat[1].toInt() + 2
                ToastUtils.showShort("每日${hour}:${minute}执行任务")
                dailyTask(hashSetOf(HourMinute(hour, minute)), dispatcher = Dispatchers.Main) {
                    ToastUtils.showShort("${TimerType.DAILY_TASK.desc}已执行")
                }.attachTo(owner)
            }
            TimerType.WEEKLY_TASK -> {
                val calendar = Calendar.getInstance(Locale.CHINA).apply {
                    timeInMillis = nowTime()
                }
                val weekValue = calendar.get(Calendar.DAY_OF_WEEK) - 1
                val week = Week.values().find { weekValue == it.code}!!
                val dataTimeFormat = DateUtils.getStringByFormat(nowTime(), "HH:mm").split(":")
                val hour = dataTimeFormat[0].toInt()
                val minute = dataTimeFormat[1].toInt() + 2
                val weekStr = when(week){
                    Week.Sunday -> "周日"
                    Week.Monday -> "周一"
                    Week.Tuesday -> "周二"
                    Week.Wednesday -> "周三"
                    Week.Thursday -> "周四"
                    Week.Friday -> "周五"
                    Week.Saturday -> "周六"
                }
                ToastUtils.showShort("每${weekStr}${hour}:${minute}执行任务")
                weeklyTask(hashSetOf(week), hashSetOf(HourMinute(hour, minute)), dispatcher = Dispatchers.Main){
                    ToastUtils.showShort("${TimerType.WEEKLY_TASK.desc}已执行")
                }.attachTo(owner)
            }
            TimerType.MONTHLY_TASK -> {
                val calendar = Calendar.getInstance(Locale.CHINA).apply {
                    timeInMillis = nowTime()
                }
                val monthValue = calendar.get(Calendar.MONDAY) + 1
                val dayValue = calendar.get(Calendar.DAY_OF_MONTH)
                val dataTimeFormat = DateUtils.getStringByFormat(nowTime(), "HH:mm").split(":")
                val hour = dataTimeFormat[0].toInt()
                val minute = dataTimeFormat[1].toInt() + 2
                ToastUtils.showShort("每年${monthValue}月${dayValue}日${hour}:${minute}执行任务")
                monthlyTask(hashSetOf(MonthDay(monthValue, dayValue)), hashSetOf(HourMinute(hour, minute)), dispatcher = Dispatchers.Main){
                    ToastUtils.showShort("${TimerType.MONTHLY_TASK.desc}已执行")
                }.attachTo(owner)
            }
        }
    }
}

enum class TimerType(val desc: String) {
    DELAY_TASK("延时任务"),
    DATE_TIME_TASK("指定日期任务"),
    INTERVAL_TASK("周期性任务"),
    DAILY_TASK("每天固定时间执行的任务"),
    WEEKLY_TASK("每周固定时间执行的任务"),
    MONTHLY_TASK("每月固定时间执行任务")
}