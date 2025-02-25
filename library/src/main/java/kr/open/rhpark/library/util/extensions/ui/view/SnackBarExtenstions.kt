package kr.open.rhpark.library.util.extensions.ui.view

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kr.open.rhpark.library.debug.logcat.Logx


/*********************
 *      SnackBar     *
 *********************/
private fun Snackbar.snackBarOption(
    @BaseTransientBottomBar.AnimationMode animMode: Int? = null,
    bgTint: Int? = null,
    bgTintStateList: ColorStateList? = null,
    textColor: Int? = null,
    textColorStateList: ColorStateList? = null,
    isGestureInsetBottomIgnored: Boolean? = null,
    actionTextColor: Int? = null,
    actionTextColorStateList: ColorStateList? = null,
    actionText: CharSequence? = null,
    action: ((View) -> Unit)? = null
) = apply{
    bgTint?.let { setBackgroundTint(it) }
    bgTintStateList?.let { setBackgroundTintList(it) }
    textColor?.let { setTextColor(it) }
    textColorStateList?.let { setTextColor(it) }
    isGestureInsetBottomIgnored?.let { setGestureInsetBottomIgnored(it) }
    animMode?.let { animationMode = it }
    actionTextColor?.let { setActionTextColor(it) }
    actionTextColorStateList?.let { setActionTextColor(it) }
    actionText?.let { setAction(it, action) }
}

public fun View.snackBarMakeShort(
    msg:CharSequence,
    @BaseTransientBottomBar.AnimationMode animMode: Int? = null,
    bgTint: Int? = null,
    bgTintStateList: ColorStateList? = null,
    textColor: Int? = null,
    textColorStateList: ColorStateList? = null,
    isGestureInsetBottomIgnored: Boolean? = null,
    actionTextColor: Int? = null,
    actionTextColorStateList: ColorStateList? = null,
    actionText: CharSequence? = null,
    action: ((View) -> Unit)? = null
): Snackbar = Snackbar.make(this, msg, Snackbar.LENGTH_SHORT).apply {
    snackBarOption(
        animMode, bgTint, bgTintStateList, textColor, textColorStateList, isGestureInsetBottomIgnored,
        actionTextColor, actionTextColorStateList, actionText, action
    )
}

public fun View.snackBarMakeLong(
    msg:CharSequence,
    @BaseTransientBottomBar.AnimationMode animMode: Int? = null,
    bgTint: Int? = null,
    bgTintStateList: ColorStateList? = null,
    textColor: Int? = null,
    textColorStateList: ColorStateList? = null,
    isGestureInsetBottomIgnored: Boolean? = null,
    actionTextColor: Int? = null,
    actionTextColorStateList: ColorStateList? = null,
    actionText: CharSequence? = null,
    action: ((View) -> Unit)? = null
): Snackbar = Snackbar.make(this, msg, Snackbar.LENGTH_LONG).apply {
    snackBarOption(
        animMode, bgTint, bgTintStateList, textColor, textColorStateList, isGestureInsetBottomIgnored,
        actionTextColor, actionTextColorStateList, actionText, action
    )
}

public fun View.snackBarMakeIndefinite(
    msg: CharSequence,
    @BaseTransientBottomBar.AnimationMode animMode: Int? = null,
    bgTint: Int? = null,
    bgTintStateList: ColorStateList? = null,
    textColor: Int? = null,
    textColorStateList: ColorStateList? = null,
    isGestureInsetBottomIgnored: Boolean? = null,
    actionTextColor: Int? = null,
    actionTextColorStateList: ColorStateList? = null,
    actionText: CharSequence? = null,
    action: ((View) -> Unit)? = null
): Snackbar = Snackbar.make(this, msg, Snackbar.LENGTH_INDEFINITE).apply {
    snackBarOption(
        animMode, bgTint, bgTintStateList, textColor, textColorStateList, isGestureInsetBottomIgnored,
        actionTextColor, actionTextColorStateList, actionText, action
    )
}


public fun View.snackBarShowShort(
    msg: CharSequence,
    @BaseTransientBottomBar.AnimationMode animationMode: Int? = null,
    bgTint: Int? = null,
    bgTintStateList: ColorStateList? = null,
    textColor: Int? = null,
    textColorStateList: ColorStateList? = null,
    isGestureInsetBottomIgnored: Boolean? = null,
    actionTextColor: Int? = null,
    actionTextColorStateList: ColorStateList? = null,
    actionText: String? = null,
    action: ((View) -> Unit)? = null
) {
    snackBarMakeShort(
        msg, animationMode, bgTint, bgTintStateList, textColor, textColorStateList,
        isGestureInsetBottomIgnored, actionTextColor, actionTextColorStateList,
        actionText, action
    ).show()
}

public fun Fragment.snackBarShowShort(
    msg: CharSequence,
    @BaseTransientBottomBar.AnimationMode animationMode: Int? = null,
    bgTint: Int? = null,
    bgTintStateList: ColorStateList? = null,
    textColor: Int? = null,
    textColorStateList: ColorStateList? = null,
    isGestureInsetBottomIgnored: Boolean? = null,
    actionTextColor: Int? = null,
    actionTextColorStateList: ColorStateList? = null,
    actionText: String? = null,
    action: ((View) -> Unit)? = null
) {
    this.view?.let {
        it.snackBarMakeShort(
            msg, animationMode, bgTint, bgTintStateList, textColor, textColorStateList,
            isGestureInsetBottomIgnored, actionTextColor, actionTextColorStateList,
            actionText, action
        ).show()
    }?: Logx.e("Fragment view is null, can not show SnackBar!!!")
}

@SuppressLint("RestrictedApi")
public fun View.snackBarShowShort(msg: CharSequence,
                                  customView: View,
                                  @BaseTransientBottomBar.AnimationMode animMode: Int? = null,
                                  isGestureInsetBottomIgnored: Boolean? = null) {
    snackBarMakeShort(msg).apply {
        val snackBarLayout = (this.view as Snackbar.SnackbarLayout)
        snackBarLayout.removeAllViews()
        snackBarLayout.setPadding(0,0,0,0)
        snackBarLayout.addView(customView)
        animMode?.let {    animationMode = it    }
        isGestureInsetBottomIgnored?.let{ setGestureInsetBottomIgnored(it)}
    }.show()
}


public fun View.snackBarShowLong(
    msg: CharSequence,
    @BaseTransientBottomBar.AnimationMode animationMode: Int? = null,
    bgTint: Int? = null,
    bgTintStateList: ColorStateList? = null,
    textColor: Int? = null,
    textColorStateList: ColorStateList? = null,
    isGestureInsetBottomIgnored: Boolean? = null,
    actionTextColor: Int? = null,
    actionTextColorStateList: ColorStateList? = null,
    actionText: String? = null,
    action: ((View) -> Unit)? = null
) {
    snackBarMakeLong(
        msg, animationMode, bgTint, bgTintStateList, textColor, textColorStateList,
        isGestureInsetBottomIgnored, actionTextColor, actionTextColorStateList,
        actionText, action
    ).show()
}

public fun Fragment.snackBarShowLong(
    msg: CharSequence,
    @BaseTransientBottomBar.AnimationMode animationMode: Int? = null,
    bgTint: Int? = null,
    bgTintStateList: ColorStateList? = null,
    textColor: Int? = null,
    textColorStateList: ColorStateList? = null,
    isGestureInsetBottomIgnored: Boolean? = null,
    actionTextColor: Int? = null,
    actionTextColorStateList: ColorStateList? = null,
    actionText: String? = null,
    action: ((View) -> Unit)? = null
) {
    this.view?.let {
        it.snackBarMakeLong(
            msg, animationMode, bgTint, bgTintStateList, textColor, textColorStateList,
            isGestureInsetBottomIgnored, actionTextColor, actionTextColorStateList,
            actionText, action
        ).show()
    }?:Logx.e("Fragment view is null, can not show SnackBar!!!")

}

@SuppressLint("RestrictedApi")
public fun View.snackBarShowLong(
    msg: CharSequence,
    customView: View,
    @BaseTransientBottomBar.AnimationMode animMode: Int? = null,
    isGestureInsetBottomIgnored: Boolean? = null
) {
    snackBarMakeLong(msg).apply {
        val snackBarLayout = (this.view as Snackbar.SnackbarLayout)
        snackBarLayout.removeAllViews()
        snackBarLayout.setPadding(0, 0, 0, 0)
        snackBarLayout.addView(customView)
        animMode?.let { animationMode = it }
        isGestureInsetBottomIgnored?.let { setGestureInsetBottomIgnored(it) }
    }.show()
}

public fun View.snackBarShowIndefinite(
    msg: CharSequence,
    @BaseTransientBottomBar.AnimationMode animationMode: Int? = null,
    bgTint: Int? = null,
    bgTintStateList: ColorStateList? = null,
    textColor: Int? = null,
    textColorStateList: ColorStateList? = null,
    isGestureInsetBottomIgnored: Boolean? = null,
    actionTextColor: Int? = null,
    actionTextColorStateList: ColorStateList? = null,
    actionText: String? = null,
    action: ((View) -> Unit)? = null
) {
    snackBarMakeIndefinite(
        msg, animationMode, bgTint, bgTintStateList, textColor, textColorStateList,
        isGestureInsetBottomIgnored, actionTextColor, actionTextColorStateList,
        actionText, action
    ).show()
}

public fun Fragment.snackBarShowIndefinite(
    msg: CharSequence,
    @BaseTransientBottomBar.AnimationMode animationMode: Int? = null,
    bgTint: Int? = null,
    bgTintStateList: ColorStateList? = null,
    textColor: Int? = null,
    textColorStateList: ColorStateList? = null,
    isGestureInsetBottomIgnored: Boolean? = null,
    actionTextColor: Int? = null,
    actionTextColorStateList: ColorStateList? = null,
    actionText: String? = null,
    action: ((View) -> Unit)? = null
) {
    this.view?.let {
        it.snackBarMakeIndefinite(
            msg, animationMode, bgTint, bgTintStateList, textColor, textColorStateList,
            isGestureInsetBottomIgnored, actionTextColor, actionTextColorStateList,
            actionText, action
        ).show()
    }?:Logx.e("Fragment view is null, can not show SnackBar!!!")
}


@SuppressLint("RestrictedApi")
public fun View.snackBarShowIndefinite(
    msg: CharSequence,
    customView: View,
    @BaseTransientBottomBar.AnimationMode animMode: Int? = null,
    isGestureInsetBottomIgnored: Boolean? = null
) {
    snackBarMakeIndefinite(msg).apply {
        val snackBarLayout = (this.view as Snackbar.SnackbarLayout)
        snackBarLayout.removeAllViews()
        snackBarLayout.setPadding(0, 0, 0, 0)
        snackBarLayout.addView(customView)
        animMode?.let { animationMode = it }
        isGestureInsetBottomIgnored?.let { setGestureInsetBottomIgnored(it) }
    }.show()
}
