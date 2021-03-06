package me.wieku.framework.gui.screen

import me.wieku.framework.graphics.drawables.containers.Container
import me.wieku.framework.math.Scaling

open class Screen : Container() {

    init {
        fillMode = Scaling.Stretch
    }

    open fun onEnter(previous: Screen?) {}
    open fun onExit(next: Screen?) {}

    open fun onResume(previous: Screen?) {}
    open fun onSuspend(next: Screen?) {}

}