package kr.open.rhpark.library.util.extensions.ui.display

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue


/****************
 * DP To PX, SP *
 ****************/
public fun Int.dpToPx(context: Context): Float = this.toFloat().dpToPx(context)
public fun Float.dpToPx(context: Context): Float =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics)

public fun Int.dpToSp(context: Context): Float = this.toFloat().dpToSp(context)
public fun Float.dpToSp(context: Context): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics
) * context.resources.configuration.fontScale


/****************
 * PX To DP, SP *
 ****************/
public fun Int.pxToDp(context: Context): Float = this.toFloat().pxToDp(context)
public fun Float.pxToDp(context: Context): Float =
    (this / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT))

public fun Int.pxToSp(context: Context): Float = this.toFloat().pxToSp(context)
public fun Float.pxToSp(context: Context): Float =
    this / context.resources.displayMetrics.density / context.resources.configuration.fontScale


/****************
 * SP To DP, PX *
 ****************/
public fun Int.spToPx(context: Context): Float = this.toFloat().spToPx(context)
public fun Float.spToPx(context: Context): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, context.resources.displayMetrics)
}

public fun Int.spToDp(context: Context): Float = this.toFloat().spToDp(context)
public fun Float.spToDp(context: Context): Float =
    this * context.resources.configuration.fontScale / context.resources.displayMetrics.density
