package kr.open.rhpark.library.system.service.controller.windowmanager

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.WindowManager
import android.view.WindowManager.LayoutParams
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.system.service.base.BaseSystemService
import kr.open.rhpark.library.system.service.controller.windowmanager.floating.data.FloatingViewCollisionsType
import kr.open.rhpark.library.system.service.controller.windowmanager.floating.drag.FloatingDragViewConfig
import kr.open.rhpark.library.system.service.controller.windowmanager.floating.drag.FloatingDragView
import kr.open.rhpark.library.system.service.controller.windowmanager.floating.fixed.FloatingFixedView

/**
 * Several functions require Android.Manifest.permission.SYSTEM_ALERT_WINDOW permission.
 */
public class FloatingViewController(context: Context, public val windowManager: WindowManager) :
    BaseSystemService(context, listOf(android.Manifest.permission.SYSTEM_ALERT_WINDOW)) {

    private var floatingDragViewInfoList: MutableList<FloatingDragViewConfig> = mutableListOf()
    private var floatingFixedView: FloatingFixedView? = null


    public fun setFloatingFixedView(floatingView: FloatingFixedView?) {

        if (!isPermissionAllGranted()) {
            Logx.e("Can not draw window Floating Icon, Permission not granted")
            return
        }

        if(floatingView == null) {
            removeFloatingFixedView()
        } else {
            addView(floatingView.view, floatingView.params)
        }
        this.floatingFixedView = floatingView
    }

    public fun getFloatingFixedView(): FloatingFixedView? = floatingFixedView

    public fun addFloatingDragView(floatingView: FloatingDragView) {

        if (!isPermissionAllGranted()) {
            Logx.e("Can not draw window Floating Icon, Permission not granted")
            return
        }

        FloatingDragViewConfig(floatingView,
            onTouchDown = { view, params ->
                floatingView.collisionsWhileTouchDown?.invoke(floatingView, getCollisionTypeWithFixedView(floatingView))
            },
            onTouchMove = { view, params ->
                updateView(view, params)
                getCollisionTypeWithFixedView(floatingView)
                floatingView.updateCollisionWhileDrag(getCollisionTypeWithFixedView(floatingView))
            },
            onTouchUp = { view, params ->
                floatingView.collisionsWhileTouchUp?.invoke(floatingView, getCollisionTypeWithFixedView(floatingView))
            }).also {
                floatingView.view.setOnTouchListener { v, event ->
                    it.setOnTouchListener(v, event)
                }
                floatingDragViewInfoList.add(it)
                addView(it.getView(), it.floatingView.params)
            }
    }

    private fun getCollisionTypeWithFixedView(floatingDragView: FloatingDragView):FloatingViewCollisionsType =
        if(isCollisionFixedView(floatingDragView)) FloatingViewCollisionsType.OCCURING
        else FloatingViewCollisionsType.UNCOLLISIONS

    private fun isCollisionFixedView(floatingDragView: FloatingDragView): Boolean = floatingFixedView?.let {
        Rect.intersects(floatingDragView.getRect(), it.getRect())
    } ?: false


    public fun updateView(view: View, params: LayoutParams) {
        params.x = params.x.coerceAtLeast(0)
        params.y = params.y.coerceAtLeast(0)
        windowManager.updateViewLayout(view, params)
    }

    public fun addView(view: View, params: LayoutParams) {
        windowManager.addView(view, params)
    }

    public fun removeFloatingDragView(floatingView: FloatingDragView) {
        floatingDragViewInfoList.find { it.floatingView == floatingView }?.let {
            it.getView().setOnTouchListener(null)
            removeView(it.getView())
            floatingDragViewInfoList.remove(it)
        }
    }

    public fun removeView(view: View) {
        windowManager.removeView(view)
    }

    public fun removeFloatingFixedView() {
        floatingFixedView?.let { removeView(it.view) }
        floatingFixedView = null
    }

    public fun removeAllFloatingView() {
        floatingDragViewInfoList.forEach { removeFloatingDragView(it.floatingView) }
        floatingDragViewInfoList.clear()
        removeFloatingFixedView()
    }

    override fun onDestroy() {
        super.onDestroy()
        removeAllFloatingView()
    }
}