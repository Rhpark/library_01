package kr.open.rhpark.library.system.service.access.display

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.RequiresApi
import kr.open.rhpark.library.system.service.base.BaseSystemService
import kr.open.rhpark.library.ui.util.getNavigationBarHeight
import kr.open.rhpark.library.ui.util.getStatusBarHeight

/**
 * This class provides information about the display of an Android device.
 * DisplayInfo 클래스는 Android 기기의 디스플레이 정보를 제공.
 *
 * @param context The application context.
 * @param context 애플리케이션 컨텍스트.
 *
 * @param windowManager The WindowManager instance.
 * @param windowManager WindowManager 인스턴스.
 */
public class DisplayInfo(context: Context, public val windowManager: WindowManager)
    : BaseSystemService(context, null) {

    /**
     * Returns the full screen size.
     * 전체 화면 크기를 반환.
     *
     * @return  The full screen size (width, height).
     * @return 전체 화면 크기 (너비, 높이)
     */
    public fun getFullScreen():Pair<Int,Int> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val metrics = getCurrentWindowMetrics().bounds
        val width = metrics.width()
        val height = metrics.height()
        Pair(width, height)
    } else {
        val displayMetrics = DisplayMetrics()
        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
            .defaultDisplay.getRealMetrics(displayMetrics)
        Pair(displayMetrics.widthPixels, displayMetrics.heightPixels)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun getCurrentWindowMetrics() = windowManager.currentWindowMetrics

    /**
     * Returns the screen size excluding the status bar and navigation bar.
     * 상태 표시줄과 네비게이션 바를 제외한 화면 크기를 반환.
     *
     * If the desired result is not obtained,
     * be used getScreenWithStatusBar() - getNavigationBarHeight(activity: Activity)
     *
     * @return The screen size (width, height).
     * @return 화면 크기 (너비, 높이).
     */
    public fun getScreen(): Pair<Int,Int> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = getCurrentWindowMetrics()
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
     * Returns the screen size excluding the navigation bar.
     * 탐색 표시줄을 제외한 화면 크기를 반환.
     *
     * @return The screen size (width, height).
     * @return 화면 크기 (너비, 높이)
     */
    public fun getScreenWithStatusBar(): Pair<Int, Int> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = getCurrentWindowMetrics()
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

    /**
     * Returns the status bar height.
     * 상태 표시줄 높이를 반환.
     *
     * @return The status bar height.
     * @return 상태 표시줄 높이.
     */
    public fun getStatusBarHeight(): Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = getCurrentWindowMetrics()
        windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars()).top

    } else {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) context.resources.getDimensionPixelSize(resourceId)
        else throw Resources.NotFoundException("Can not find status bar height. you can try call method getStatusBarHeight(activity: Activity).")
    }

    public fun getStatusBarHeight(activity: Activity):Int = activity.getStatusBarHeight()


    /**
     * Returns the navigation bar height.
     * 탐색 표시줄 높이를 반환.
     *
     * @return The navigation bar height.
     * @return 탐색 표시줄 높이.
     */
    public fun getNavigationBarHeight(): Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = getCurrentWindowMetrics()
        windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars()).bottom

    } else {
        val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) context.resources.getDimensionPixelSize(resourceId)
        else throw Resources.NotFoundException("Can not find navigation bar height. you can try call method getNavigationBarHeight(activity: Activity).")
    }

    public fun getNavigationBarHeight(activity: Activity): Int = activity.getNavigationBarHeight()
}