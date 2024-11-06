package kr.open.rhpark.library.system.service.access

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowInsets
import android.view.WindowManager
import kr.open.rhpark.library.system.service.base.BaseSystemService
import kr.open.rhpark.library.ui.util.getNavigationBarHeight
import kr.open.rhpark.library.ui.util.getStatusBarHeight

public class DisplayInfo(private val context: Context, private val windowManager: WindowManager)
    : BaseSystemService(context, null) {

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
     * If the desired result is not obtained,
     * be used getScreenWithStatusBar() - getNavigationBarHeight(activity: Activity)
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
            return@run Pair(first, second - statusBarHeight)
        }
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

    public fun getNavigationBarHeight(activity: Activity): Int = activity.getNavigationBarHeight()
}