package com.frame.basic.base.ktx

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.core.app.ComponentActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * @Author: lee
 * @Date: 2021/7/12-11:24
 * @Des: ViewBindingKTX
 */
@MainThread
inline fun <reified VB : ViewBinding> ComponentActivity.viewBindings() = lazy {
    inflateBinding<VB>(layoutInflater).apply {
        if (this is ViewDataBinding) lifecycleOwner = this@viewBindings
    }
}

/*@MainThread
inline fun <reified VB : ViewBinding> Fragment.viewBindings() =  lazy {
    inflateBinding<VB>(layoutInflater).apply {
        if (this is ViewDataBinding) lifecycleOwner = this@viewBindings
    }
}*/
@MainThread
inline fun <reified VB : ViewBinding> Fragment.viewBindings() =
    FragmentBindingDelegate<VB> { inflateBinding(layoutInflater) }
/*
@MainThread
inline fun <reified VB : ViewBinding> Fragment.viewBindings() =
    FragmentBindingDelegate<VB> { requireView().viewBindings() }.apply {
        if (this is ViewDataBinding) lifecycleOwner = this@viewBindings
    }
*/

@MainThread
inline fun <reified VB : ViewBinding> Dialog.viewBindings() = lazy {
    inflateBinding<VB>(layoutInflater).also { setContentView(it.root) }
}

@MainThread
inline fun <reified VB : ViewBinding> ViewGroup.viewBindings(attachToParent: Boolean = true) =
    lazy {
        inflateBinding<VB>(
            LayoutInflater.from(context),
            if (attachToParent) this else null,
            attachToParent
        )
    }

@MainThread
inline fun <reified VB : ViewBinding> inflateBinding(layoutInflater: LayoutInflater) =
    VB::class.java.getMethod("inflate", LayoutInflater::class.java)
        .invoke(null, layoutInflater) as VB

@MainThread
inline fun <reified VB : ViewBinding> inflateBinding(parent: ViewGroup) =
    inflateBinding<VB>(LayoutInflater.from(parent.context), parent, false)

@MainThread
inline fun <reified VB : ViewBinding> inflateBinding(
    layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean
) =
    VB::class.java.getMethod(
        "inflate",
        LayoutInflater::class.java,
        ViewGroup::class.java,
        Boolean::class.java
    )
        .invoke(null, layoutInflater, parent, attachToParent) as VB

@MainThread
inline fun <reified VB : ViewBinding> View.viewBindings() =
    (VB::class.java.getMethod("bind", View::class.java).invoke(null, this) as VB)

@MainThread
inline fun Fragment.doOnDestroyView(crossinline block: () -> Unit) =
    viewLifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroyView() {
            block.invoke()
        }
    })

enum class Method { BIND, INFLATE }

interface BindingLifecycleOwner {
    fun onDestroyViewBinding(destroyingBinding: ViewBinding)
}

class FragmentBindingDelegate<VB : ViewBinding>(private val block: () -> VB) :
    ReadOnlyProperty<Fragment, VB> {
    private var binding: VB? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): VB {
        if (binding == null) {
            binding = block().also {
                if (it is ViewDataBinding) it.lifecycleOwner = thisRef.viewLifecycleOwner
            }
            thisRef.doOnDestroyView {
                if (thisRef is BindingLifecycleOwner) thisRef.onDestroyViewBinding(binding!!)
                binding = null
            }
        }
        return binding!!
    }
}