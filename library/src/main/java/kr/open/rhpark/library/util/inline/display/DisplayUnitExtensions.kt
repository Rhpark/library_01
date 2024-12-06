package kr.open.rhpark.library.util.inline.display

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue


/****************
 * DP To PX, SP *
 ****************/

public inline fun Number.dpToPx(context: Context): Float =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics)

public inline fun Number.dpToSp(context: Context): Float =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(),
        context.resources.displayMetrics) * context.resources.configuration.fontScale



/****************
 * PX To DP, SP *
 ****************/

public inline fun Number.pxToDp(context: Context): Float =
    (this.toFloat() / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT))

public inline fun Number.pxToSp(context: Context): Float =
    (this.toFloat() / context.resources.displayMetrics.density / context.resources.configuration.fontScale)



/****************
 * SP To DP, PX *
 ****************/

public inline fun Number.spToPx(context: Context): Float =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), context.resources.displayMetrics)

public inline fun Number.spToDp(context: Context): Float =
    (this.toFloat() * context.resources.configuration.fontScale / context.resources.displayMetrics.density)
