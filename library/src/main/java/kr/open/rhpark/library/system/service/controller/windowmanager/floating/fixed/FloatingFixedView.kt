package kr.open.rhpark.library.system.service.controller.windowmanager.floating.fixed

import android.graphics.PixelFormat
import android.graphics.Rect
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager.LayoutParams
import kr.open.rhpark.library.util.extensions.sdk_version.checkSdkVersion

public open class FloatingFixedView(
    public val view: View,
    public val startX: Int,
    public val startY: Int
) {

    public val params: LayoutParams = getFloatingLayoutParam().apply {
        gravity = Gravity.TOP or Gravity.LEFT
        this.x = startX
        this.y = startY
    }

    private fun getFloatingLayoutParam(): LayoutParams = checkSdkVersion(Build.VERSION_CODES.O,{
        LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT,
            LayoutParams.TYPE_APPLICATION_OVERLAY,
            LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
    },{
        LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT,
            LayoutParams.TYPE_SYSTEM_ALERT + LayoutParams.TYPE_PHONE,
            LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
    })

    public fun getRect(): Rect {
        val width = if (view.width > 0) view.width else view.measuredWidth
        val height = if (view.height > 0) view.height else view.measuredHeight
        return Rect(params.x, params.y, params.x + width, params.y + height)
    }
}