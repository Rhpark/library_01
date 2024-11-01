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


/****************
 * DP To PX, SP *
 ****************/
public fun Int.dpToPx(context: Context): Float = this.toFloat().dpToPx(context)
public fun Float.dpToPx(context: Context): Float =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics)

public fun Int.dpToSp(context:Context): Float = this.toFloat().dpToSp(context)
public fun Float.dpToSp(context:Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics
    ) * context.resources.configuration.fontScale
}


/****************
 * PX To DP, SP *
 ****************/
public fun Int.pxToDp(context: Context): Float = this.toFloat().pxToDp(context)
public fun Float.pxToDp(context: Context): Float =
    (this / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT))

public fun Int.pxToSp(context: Context): Float = this.toFloat().pxToSp(context)
public fun Float.pxToSp(context: Context): Float = this / context.resources.configuration.fontScale


/****************
 * SP To DP, PX *
 ****************/
public fun Int.spToPx(context: Context): Float = this.toFloat().spToPx(context)
public fun Float.spToPx(context: Context): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, context.resources.displayMetrics)
}

public fun Int.spToDp(context: Context): Float = this.toFloat().spToDp(context)
public fun Float.spToDp(context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, this, context.resources.displayMetrics
    ) / context.resources.displayMetrics.density
}



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
