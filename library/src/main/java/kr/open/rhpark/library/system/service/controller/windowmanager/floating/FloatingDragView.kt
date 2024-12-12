package kr.open.rhpark.library.system.service.controller.windowmanager.floating

import android.view.View
import kr.open.rhpark.library.system.service.base.DataUpdate

public class FloatingDragView(
    view: View,
    startX: Int,
    startY: Int,
    public var collisionsWhileDrag: ((CollisionsType) -> Unit)? = null,
    public var collisionsWhileTouchUp: ((CollisionsType) -> Unit)? = null,
    public var collisionsWhileTouchDown: ((CollisionsType) -> Unit)? = null
) : FloatingView(view, startX, startY) {

    private val updateCollisionWhileDrag = DataUpdate(CollisionsType.UNCOLLISIONS) { type->
        collisionsWhileDrag?.invoke(type)
    }

    public fun updateCollisionWhileDrag(collisionsType: CollisionsType) {
        updateCollisionWhileDrag.update(collisionsType)
    }

    private val updateCollisionWhileUp = DataUpdate(CollisionsType.UNCOLLISIONS) { type->
        collisionsWhileTouchUp?.invoke(type)
    }

    public fun updateCollisionWhileUp(collisionsType: CollisionsType) {
        updateCollisionWhileUp.update(collisionsType)
    }

    public enum class CollisionsType {
        OCCURING,
        UNCOLLISIONS,
    }
}