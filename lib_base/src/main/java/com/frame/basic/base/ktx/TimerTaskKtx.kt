package com.frame.basic.base.ktx

import androidx.annotation.IntRange
import androidx.annotation.MainThread
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.util.*

/**
 * 定时任务
 * @param runNow 是否立即执行
 * @param bindOwner 是否绑定VM生命周期
 */
@MainThread
fun task(
    task: Task,
    runNow: Boolean = false
): Task {
    TimerManager.put(task, runNow)
    return task
}

/**
 * 延时任务
 * 延迟一定时间执行任务
 * @param delay 延时时长
 * @param runNow 是否立即执行
 * @param dispatcher 执行线程
 * @param callback 执行回调
 */
@MainThread
fun delayTask(
    @IntRange(from = 1) delay: Long,
    delayUnit: TimeUnit = TimeUnit.SECONDS,
    runNow: Boolean = false,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    callback: (executeCount: Long) -> Unit
) = task(DelayTask(delay, delayUnit, dispatcher, callback), runNow)

/**
 * 指定日期任务
 * @param date 指定日期 yyyy-MM-dd HH:mm
 * @param dispatcher 执行线程
 * @param runNow 是否立即执行
 */
@MainThread
fun dateTimeTask(
    date: Date,
    runNow: Boolean = false,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    callback: (executeCount: Long) -> Unit
) = task(DateTimeTask(date, dispatcher, callback), runNow)

/**
 * 周期性任务
 * 每隔一定时间执行一次
 * @param startTime 限定开始执行时间 默认当前时间
 * @param endTime 限定结束执行时间 默认永久
 * @param interval 间隔执行时间
 * @param maxExecutes 限定最大执行次数 默认不限制
 * @param dispatcher 执行线程
 * @param runNow 是否立即执行
 */
@MainThread
fun intervalTask(
    @IntRange(from = 1)
    interval: Long,
    intervalUnit: TimeUnit = TimeUnit.SECONDS,
    @IntRange(from = 1)
    startTime: Long = TimerManager.getNowTime(),
    @IntRange(from = 1)
    endTime: Long = Long.MAX_VALUE,
    @IntRange(from = 1)
    maxExecutes: Long = Long.MAX_VALUE,
    runNow: Boolean = false,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    callback: (executeCount: Long) -> Unit
) = task(
    IntervalTask(
        interval,
        intervalUnit,
        startTime,
        endTime,
        maxExecutes,
        dispatcher,
        callback
    ), runNow
)

/**
 * 每天固定时间执行的任务
 * @param hourMinutes 时分集合
 * @param runNow 是否立即执行
 * @param dispatcher 执行线程
 */
@MainThread
fun dailyTask(
    hourMinutes: HashSet<HourMinute>,
    runNow: Boolean = false,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    callback: (executeCount: Long) -> Unit
) = task(DailyTask(hourMinutes, dispatcher, callback), runNow)

/**
 * 每周固定时间执行的任务
 * @param weeks 周集合
 * @param hourMinutes 时分集合
 * @param runNow 是否立即执行
 * @param dispatcher 执行线程
 */
@MainThread
fun weeklyTask(
    weeks: HashSet<Week>,
    hourMinutes: HashSet<HourMinute>,
    runNow: Boolean = false,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    callback: (executeCount: Long) -> Unit
) = task(WeeklyTask(weeks, hourMinutes, dispatcher, callback), runNow)

/**
 * 每月固定时间执行任务
 * @param monthDays 月日集合
 * @param hourMinutes 时分集合
 * @param runNow 是否立即执行
 * @param dispatcher 执行线程
 */
@MainThread
fun monthlyTask(
    monthDays: HashSet<MonthDay>,
    hourMinutes: HashSet<HourMinute>,
    runNow: Boolean = false,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    callback: (executeCount: Long) -> Unit
) = task(MonthlyTask(monthDays, hourMinutes, dispatcher, callback), runNow)

/**
 * 获取当前时间
 */
fun nowTime() = TimerManager.getNowTime()

