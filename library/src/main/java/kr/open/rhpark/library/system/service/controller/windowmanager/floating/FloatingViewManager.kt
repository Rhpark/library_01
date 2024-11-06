package kr.open.rhpark.library.system.service.controller.windowmanager.floating

import android.graphics.Point
import android.graphics.PointF
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager.LayoutParams
import kotlin.math.absoluteValue

internal class FloatingViewManager(
    private val floatingView: WindowManagerFloatingView,
    private val params: LayoutParams,
    private val onTouchMove: (view:View, params: LayoutParams) -> Unit
) {

    /**
     * Value for Drag
     */
    private var initialTouchDownPosition :Point = Point(0,0)
    private var initialTouchDragPosition:PointF = PointF(0f,0f)


    /**
     * Value for Click
     */
    private val clickThreshold = 15
    private var isDragging = false
    private var initialClickDownPosition:PointF = PointF(0f,0f)

    init {
        if(floatingView.isDraggable) {
            floatingView.view.setOnTouchListener { v, event ->
                when(event.action) {
                    MotionEvent.ACTION_DOWN ->{
                        setLocation(event.rawX,event.rawY)
                        true
                    }

                    MotionEvent.ACTION_MOVE->{
                        changeLocation(event.rawX, event.rawY)
                        true
                    }

                    MotionEvent.ACTION_UP -> {
                        if(!isDragging) { floatingView.view.performClick() }
                        true
                    }
                    else -> false
                }
            }
        } else {
            floatingView.view.performClick()
        }
    }

    private fun setLocation(x:Float, y:Float) {
        initDrag(x,y)
        initClick(x,y)
    }

    private fun initDrag(x:Float, y:Float) {
        initialTouchDownPosition.set(params.x, params.y)
        initialTouchDragPosition.set(x,y)
    }

    private fun initClick(x:Float, y:Float) {
        isDragging = false
        initialClickDownPosition.set(x,y)
    }

    private fun changeLocation(x:Float, y:Float) {
        touchDragChangeLocation(x,y)
        clickDragChanged(x,y)
    }

    private fun touchDragChangeLocation(x:Float, y:Float) {
        params.x = initialTouchDownPosition.x + (x - initialTouchDragPosition.x).toInt()
        params.y = initialTouchDownPosition.y + (y - initialTouchDragPosition.y).toInt()
        onTouchMove(floatingView.view, params)
    }

    private fun clickDragChanged(x:Float, y:Float) {
        if ((x.absoluteValue - initialClickDownPosition.x.toInt()) > clickThreshold
            || (y.absoluteValue - initialClickDownPosition.y.toInt()) > clickThreshold) {
            isDragging = true
        }
    }

    public fun getView():View = floatingView.view
}