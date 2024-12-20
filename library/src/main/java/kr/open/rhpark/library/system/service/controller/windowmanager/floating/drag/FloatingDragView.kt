package kr.open.rhpark.library.system.service.controller.windowmanager.floating.drag

import android.view.View
import kr.open.rhpark.library.system.service.base.DataUpdate
import kr.open.rhpark.library.system.service.controller.windowmanager.floating.vo.FloatingViewCollisionsType
import kr.open.rhpark.library.system.service.controller.windowmanager.floating.fixed.FloatingFixedView

public class FloatingDragView(
    view: View,
    startX: Int,
    startY: Int,
    public var collisionsWhileTouchDown: ((FloatingDragView, FloatingViewCollisionsType) -> Unit)? = null,
    public var collisionsWhileDrag: ((FloatingDragView, FloatingViewCollisionsType) -> Unit)? = null,
    public var collisionsWhileTouchUp: ((FloatingDragView, FloatingViewCollisionsType) -> Unit)? = null
) : FloatingFixedView(view, startX, startY) {

    private val updateCollisionWhileDrag = DataUpdate(FloatingViewCollisionsType.UNCOLLISIONS) { type ->
            collisionsWhileDrag?.invoke(this, type)
        }

    public fun updateCollisionWhileDrag(floatingViewCollisionsType: FloatingViewCollisionsType) {
        updateCollisionWhileDrag.update(floatingViewCollisionsType)
    }
}