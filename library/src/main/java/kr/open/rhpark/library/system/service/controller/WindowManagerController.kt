package kr.open.rhpark.library.system.service.controller

import android.app.Activity
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.system.service.base.BaseSystemService
import kr.open.rhpark.library.ui.util.getStatusBarHeight


public class WindowManagerController(private val context: Context, private val windowManager: WindowManager)
    : BaseSystemService(context, arrayOf(android.Manifest.permission.SYSTEM_ALERT_WINDOW)) {

    private var defaultViewInfo: Pair<View, WindowManager.LayoutParams>? = null
    private var defaultSystemDialogInfo: Pair<View, WindowManager.LayoutParams>? = null
    private var defaultSystemWindowInfo: Pair<View, WindowManager.LayoutParams>? = null

    /**
     * return pair<width, height>
     */
    public fun getFullScreen():Pair<Int,Int> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val metrics = windowManager.currentWindowMetrics.bounds
        val width = metrics.width()
        val height = metrics.height()
        Pair(width, height)
    } else {
        val displayMetrics = DisplayMetrics()
        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
            .defaultDisplay.getRealMetrics(displayMetrics)
        Pair(displayMetrics.widthPixels, displayMetrics.heightPixels)
    }

    /**
     * FullScreen - StatusBar Height - NavigationBar Height
     *
     * return pair<width, height>
     */
    public fun getScreen(): Pair<Int,Int> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = windowManager.currentWindowMetrics
        val insets = windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())

        val width = windowMetrics.bounds.width() - (insets.left + insets.right)
        val height = windowMetrics.bounds.height() - (insets.bottom + insets.top)
        Pair(width, height)
    } else {
        getScreenWithStatusBar().run {
            val statusBarHeight = getStatusBarHeight()
            Pair(first, second - statusBarHeight)
        }
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        Logx.d("windowManager.defaultDisplay ${windowManager.defaultDisplay.height}")
        Pair(displayMetrics.widthPixels, displayMetrics.heightPixels)
    }

    /**
     * FullScreen - NavigationBar Height
     *
     * return pair<width, height>
     */
    public fun getScreenWithStatusBar(): Pair<Int, Int> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = windowManager.currentWindowMetrics
            val insets = windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())

            val width = windowMetrics.bounds.width()
            val height = windowMetrics.bounds.height() - (insets.bottom)
            return Pair(width, height)
        } else {
            context.resources.displayMetrics.run {
                return Pair(widthPixels, heightPixels)
            }
        }
    }

    public fun getStatusBarHeight(): Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = windowManager.currentWindowMetrics
        windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars()).top

    } else {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) context.resources.getDimensionPixelSize(resourceId)
        else throw Exception("Can not find status bar height. you can try call method getStatusBarHeight(activity: Activity).")
    }

    public fun getStatusBarHeight(activity: Activity):Int = activity.getStatusBarHeight()

    public fun getNavigationBarHeight(): Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = windowManager.currentWindowMetrics
        windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars()).bottom

    } else {
        val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) context.resources.getDimensionPixelSize(resourceId)
        else throw Exception("Can not find navigation bar height. you can try call method getNavigationBarHeight(activity: Activity).")
    }

    public fun addView(view: View) {

        if(defaultViewInfo != null) throw Exception ("Already addView, Please check init.")

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        windowManager.addView(view, params)
        defaultViewInfo = Pair(view, params)
    }

    public fun updateView(xOffset: Int, yOffset: Int) {

        if(defaultViewInfo == null) throw Exception ("Can not find view and params, Please before addView")

        defaultViewInfo?.let {
            it.second.x = xOffset
            it.second.y = yOffset
            windowManager.updateViewLayout(it.first,it.second)
        }
    }

    public fun removeView() {
        if(defaultViewInfo == null) throw Exception("Already remove view.")

        defaultViewInfo?.let {
            windowManager.removeView(it.first)
        }
        defaultViewInfo = null
    }

    /**
     * Request permission android.Manifest.permission.SYSTEM_ALERT_WINDOW
     */
    public fun addSystemDialogView(view: View) {

        if(defaultSystemDialogInfo != null) throw Exception("Already System Dialog View.")

        val params : WindowManager.LayoutParams = if(Build.VERSION.SDK_INT >= 26) {
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,  // 또는 TYPE_APPLICATION_OVERLAY
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        } else {
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        }

        defaultSystemDialogInfo = Pair(view, params)

        windowManager.addView(view, params)
    }

    public fun removeSystemDialogView() {
        if (defaultSystemDialogInfo == null) throw Exception("Can not find system Dialog view")

        defaultSystemDialogInfo?.let {
            windowManager.removeView(it.first)
        }
        defaultSystemDialogInfo = null
    }

    public fun addSystemWindow(view: View) {

        if(defaultSystemWindowInfo != null) throw Exception("Already System Window View.")

        val params = if(Build.VERSION.SDK_INT < 26) {
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT + WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        } else if(Build.VERSION.SDK_INT in 26..28) {
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,  // 또는 다른 시스템 윈도우 유형
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        } else {
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,  // 또는 다른 시스템 윈도우 유형
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        }

        defaultSystemWindowInfo = Pair(view, params)
        windowManager.addView(view, params)
    }

    public fun removeSystemView() {
        if (defaultSystemWindowInfo == null) throw Exception("Can not find system Window view")

        defaultSystemWindowInfo?.let {
            windowManager.removeView(it.first)
        }
        defaultSystemWindowInfo = null
    }
}