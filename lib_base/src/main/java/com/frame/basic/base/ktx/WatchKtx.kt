package com.frame.basic.base.ktx

import androidx.lifecycle.MutableLiveData
import com.frame.basic.base.mvvm.vm.CoreVM

/**
 * @Description:    LiveData观察者，可自定义观察对象，及时更新自己
 * @Author:         fanj
 * @CreateDate:     2022/6/10 15:01
 * @Version:
 */
inline fun <reified T, DATA> Lazy<MutableLiveData<T>>.watch(
    targetVM: CoreVM,
    vmWatch: VMWatchOwner<T, DATA>
): Lazy<MutableLiveData<T>> {
    vmWatch.observe(targetVM, this.value)
    return this
}

inline fun <reified T, DATA> Lazy<MutableLiveData<T>>.watch(
    targetVM: CoreVM,
    data: MutableLiveData<DATA>?,
    crossinline onChange: (data: DATA?, self: MutableLiveData<T>) -> Unit
): Lazy<MutableLiveData<T>> {
    data?.let { liveData ->
        targetVM.safeObserveForever(liveData) {
            onChange(it as? DATA, this.value)
        }
    }
    return this
}

inline fun <reified T, DATA> MutableLiveData<T>.watch(
    targetVM: CoreVM,
    vmWatch: VMWatchOwner<T, DATA>
): MutableLiveData<T> {
    vmWatch.observe(targetVM, this)
    return this
}

inline fun <reified T, DATA> MutableLiveData<T>.watch(
    targetVM: CoreVM,
    data: MutableLiveData<DATA>?,
    crossinline onChange: (data: DATA?, self: MutableLiveData<T>) -> Unit
): MutableLiveData<T> {
    data?.let { liveData ->
        targetVM.safeObserveForever(liveData) {
            onChange(it as? DATA, this)
        }
    }
    return this
}

interface VMWatchOwner<W, T> {
    fun observe(coreVM: CoreVM, to: MutableLiveData<W>) {
        watch()?.let { watcher ->
            coreVM.safeObserveForever(watcher) {
                onChange(it as? T, to)
            }
        }
    }

    fun watch(): MutableLiveData<T>?
    fun onChange(data: T?, to: MutableLiveData<W>)
}