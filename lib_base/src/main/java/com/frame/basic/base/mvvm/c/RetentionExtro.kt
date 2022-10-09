package com.frame.basic.base.mvvm.c

enum class CoordinatorPluginCollapseMode(val mode: Int) {
    /** The view will act as normal with no collapsing behavior.  */
    COLLAPSE_MODE_OFF(0),
    /** The view will pin in place until it reaches the bottom of the [ ]. */
    COLLAPSE_MODE_PIN(1),
    /** The view will scroll in a parallax fashion. See [.setParallaxMultiplier] to change the multiplier used. */
    COLLAPSE_MODE_PARALLAX(2)
}