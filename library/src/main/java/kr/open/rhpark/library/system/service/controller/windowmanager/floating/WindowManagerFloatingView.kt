package kr.open.rhpark.library.system.service.controller.windowmanager.floating

import android.graphics.Point
import android.view.View

public data class WindowManagerFloatingView(
    public val view: View,
    public val isDraggable: Boolean = true
) {
    private var position = Point(0,0)

    public fun setStartPosition(x: Int, y: Int) {
        position.x = x
        position.y = y
    }

    public fun getStartPosition(): Point = position
}