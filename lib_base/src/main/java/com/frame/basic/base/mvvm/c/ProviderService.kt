package com.frame.basic.base.mvvm.c

import com.blankj.utilcode.util.GsonUtils
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.KClass

private val loaderMap by lazy {
    HashMap<Class<*>, ServiceLoader<*>>()
}

/**
 * 注册服务
 */
fun register(service: Class<*>): ServiceLoader<*> {
    var loader = loaderMap[service]
    if (loader == null) {
        loader = ServiceLoader.load(service)
        loaderMap[service] = loader
    }
    return loader!!
}

/**
 * 获取服务
 * 注意：一个Service只能对应一个实现服务，如果多个也只会调用第一个
 */
inline fun <reified T> Any.getServiceProvider(): T? {
    val loader = register(T::class.java) as ServiceLoader<T>
    if (loader.iterator().hasNext()) {
        return loader.iterator().next()
    }
    return null
}

/**
 * json类型适配文档注释
 * 在实现层的方法参数和返回值标记对象类，方便调用方查看数据类型，并做响应适配
 */
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.SOURCE)
annotation class ServiceBean(val value: KClass<*>)

/**
 * 一般类型service适配
 */
inline fun <reified T> Any.toServiceBean(): T {
    return GsonUtils.fromJson(GsonUtils.toJson(this), T::class.java)
}

/**
 * Collection类型service适配
 */
fun <T> Collection<*>.toServiceBean(): Collection<T> {
    return GsonUtils.fromJson(GsonUtils.toJson(this), object : TypeToken<Collection<T>>() {}.type)
}

/**
 * Array类型service适配
 */
fun <T> Array<*>.toServiceBean(): Array<T> {
    return GsonUtils.fromJson(GsonUtils.toJson(this), object : TypeToken<Array<T>>() {}.type)
}

/**
 * Map类型service适配
 */
fun <T, K> Map<*, *>.toServiceBean(): Map<T, K> {
    return GsonUtils.fromJson(GsonUtils.toJson(this), object : TypeToken<Map<T, K>>() {}.type)
}
