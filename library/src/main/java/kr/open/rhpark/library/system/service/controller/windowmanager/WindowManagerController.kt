package kr.open.rhpark.library.system.service.controller.windowmanager

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.view.WindowManager.LayoutParams
import kr.open.rhpark.library.system.service.base.BaseSystemService
import kr.open.rhpark.library.system.service.controller.windowmanager.floating.FloatingViewManager
import kr.open.rhpark.library.system.service.controller.windowmanager.floating.WindowManagerFloatingView

/**
 * Several functions require Android.Manifest.permission.SYSTEM_ALERT_WINDOW permission.
 */
public class WindowManagerController(context: Context, public val windowManager: WindowManager)
    : BaseSystemService(context, listOf(android.Manifest.permission.SYSTEM_ALERT_WINDOW)) {

    private var floatingViewInfoList: MutableList<FloatingViewManager> = mutableListOf()

    public fun addFloatingView(floatingView: WindowManagerFloatingView) {

        if(!isPermissionAllGranted()) { return }

        val params = getFloatingLayoutParam().apply {
            gravity = android.view.Gravity.TOP or android.view.Gravity.LEFT
            floatingView.getStartPosition().also { position->
                this.x = position.x
                this.y = position.y
            }
        }

        addView(floatingView.view, params)
        val floatingAddConfig = FloatingViewManager(floatingView, params) { view, param ->
            updateView(view, param)
        }

        floatingViewInfoList.add(floatingAddConfig)
    }

    public fun updateView(view: View, params: LayoutParams) {
        windowManager.updateViewLayout(view, params)
    }

    public fun addView(view:View, params: LayoutParams) {
        windowManager.addView(view, params)
    }

    public fun removeView(view:View) {
        windowManager.removeView(view)
    }

    public fun removeAllFloatingView() {
        floatingViewInfoList.forEach { windowManager.removeView(it.getView()) }
        floatingViewInfoList.clear()
    }

    public fun getFloatingLayoutParam(): LayoutParams = if (Build.VERSION.SDK_INT < 26) {
        LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT,
            LayoutParams.TYPE_SYSTEM_ALERT + LayoutParams.TYPE_PHONE,
            LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
    } else {
        LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT,
            LayoutParams.TYPE_APPLICATION_OVERLAY,
            LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
    }
}