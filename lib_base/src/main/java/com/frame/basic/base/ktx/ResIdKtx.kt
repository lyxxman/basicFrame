package com.frame.basic.base.ktx

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

inline fun <reified VB : ViewBinding> Int.viewBindings(
    activity: Activity,
    root: ViewGroup? = null,
    attachToRoot: Boolean = false
): VB {
    return viewBindings(activity.layoutInflater, root, attachToRoot)
}

inline fun <reified VB : ViewBinding> Int.viewBindings(
    fragment: Fragment,
    root: ViewGroup? = null,
    attachToRoot: Boolean = false
): VB {
    return viewBindings(fragment.layoutInflater, root, attachToRoot)
}

inline fun <reified VB : ViewBinding> Int.viewBindings(
    layoutInflater: LayoutInflater,
    root: ViewGroup? = null,
    attachToRoot: Boolean = false
): VB {
    return layoutInflater.inflate(this, root, attachToRoot).viewBindings()
}