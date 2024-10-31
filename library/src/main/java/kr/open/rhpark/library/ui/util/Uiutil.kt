package kr.open.rhpark.library.ui.util

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.WindowInsets
import kr.open.rhpark.library.debug.logcat.Logx


public fun Int.dpToPx(context: Context): Float = (this * context.resources.displayMetrics.density)

public fun Float.pxToDp(context: Context): Float =
    (this / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT))

public fun Float.spToPx(context: Context): Float {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, context.resources.displayMetrics)
    } else {
        this * context.resources.displayMetrics.scaledDensity // 지원 중단됨
    }
}

public fun Int.spToPx(context: Context): Float = this.toFloat().spToPx(context)

public fun Float.pxToSp(context: Context): Float {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX, this, context.resources.displayMetrics
        ) / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 1f, context.resources.displayMetrics) // SP 변환에 TypedValue 사용
    } else {
        this / context.resources.displayMetrics.scaledDensity // 지원 중단됨; 이전 버전용
    }
}

public fun Int.pxToSp(context: Context): Float = this.toFloat().pxToSp(context)

public fun Activity.getStatusBarHeight(): Int =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.decorView.getRootWindowInsets().getInsets(WindowInsets.Type.statusBars()).top
    } else {
        val rectangle = Rect()
        window.decorView.getWindowVisibleDisplayFrame(rectangle)
        rectangle.top
    }



public fun Activity.getNavigationBarHeight(): Int =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val res = window.decorView.getRootWindowInsets().getInsets(WindowInsets.Type.navigationBars())
        Logx.d("${res.top}, ${res.bottom}")
        res.bottom
    } else {
        val rootView = window.decorView.rootView
        val contentViewHeight = findViewById<View>(android.R.id.content).height
        val otherViewHeight = rootView.height - contentViewHeight
        val navigationBarHeight = otherViewHeight - getStatusBarHeight()
        navigationBarHeight
    }
