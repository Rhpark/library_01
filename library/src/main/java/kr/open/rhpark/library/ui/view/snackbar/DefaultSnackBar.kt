package kr.open.rhpark.library.ui.view.snackbar

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.View
import com.google.android.material.snackbar.Snackbar


public class DefaultSnackBar(private val view: View) {

    private var animation: Int? = null

    private var textMaxLines: Int? = null

    /** TextColor **/
    private var textColor: Int? = null
    private var textColorStateList: ColorStateList? = null

    /** Background Color **/
    private var backgroundTint: Int? = null
    private var backgroundTintStateList: ColorStateList? = null

    /** Action Text Color **/
    private var actionTextColor :Int ?= null
    private var actionTextColorStateList : ColorStateList?= null

    private var isGestureInsetBottomIgnored = true

    private var customView: View? = null

    /**
     * ex) Snackbar.ANIMATION_MODE_FADE or Snackbar.ANIMATION_MODE_SLIDE
     */
    public fun setAnimation(animation:Int?) { this.animation = animation }

    public fun setActionTextColor(actionTextColor: Int?) {
        this.actionTextColor = actionTextColor
        actionTextColorStateList = null
    }

    public fun setActionTextColor(actionTextColorStateList: ColorStateList?) {
        this.actionTextColorStateList = actionTextColorStateList
        this.actionTextColor = null
    }

    public fun setBackgroundTint(backgroundTine: Int?) {
        this.backgroundTint = backgroundTine
        backgroundTintStateList = null
    }

    public fun setBackgroundTint(backgroundTineStateList: ColorStateList?) {
        this.backgroundTintStateList = backgroundTineStateList
        backgroundTint = null
    }

    public fun setCustomView(customView: View) { this.customView = customView }

    public fun setGestureInsetBottomIgnored(isGestureInsetBottomIgnored: Boolean) {
        this.isGestureInsetBottomIgnored = isGestureInsetBottomIgnored
    }

    /**
     * Select TextColorStateList or TextColor
     */
    public fun setTextColor(textColorStateList: ColorStateList?) {
        this.textColorStateList = textColorStateList
        textColor = null
    }

    public fun setTextColor(textColor: Int?) {
        this.textColor = textColor
        this.textColorStateList = null
    }

    public fun setTextMaxLines(textMaxLines: Int?) { this.textMaxLines = textMaxLines }

    public fun showShort(msg: String) {   make(msg, Snackbar.LENGTH_SHORT).show()    }
    public fun showShort(resId: Int)  {    make(resId, Snackbar.LENGTH_SHORT).show()  }

    public fun showLong(msg: String)  {   make(msg, Snackbar.LENGTH_LONG).show() }
    public fun showLong(resId: Int)   {   make(resId, Snackbar.LENGTH_LONG).show()   }

    public fun showIndefinite(msg: String)  {  make(msg, Snackbar.LENGTH_INDEFINITE)    }
    public fun showIndefinite(resId: Int)   {   make(resId, Snackbar.LENGTH_INDEFINITE) }

    private fun make(msg: String, lengthType: Int) =
        Snackbar.make(view, msg, lengthType).apply { defaultSet(this) }

    private fun make(resId: Int, lengthType: Int) =
        Snackbar.make(view, resId, lengthType).apply { defaultSet(this) }

    @SuppressLint("RestrictedApi")
    private fun defaultSet(snackBar: Snackbar) {
        snackBar.apply {
            actionTextColor?.let { setActionTextColor(it) }
            actionTextColorStateList?.let { setActionTextColor(it) }

            animation?.let { animationMode = it }

            backgroundTint?.let { setBackgroundTint(it) }
            backgroundTintStateList?.let { setBackgroundTintList(it) }

            customView?.let { (this@DefaultSnackBar.view as Snackbar.SnackbarLayout).addView(it) }

            isGestureInsetBottomIgnored = this@DefaultSnackBar.isGestureInsetBottomIgnored

            textColor?.let { setTextColor(it) }
            textColorStateList?.let { setTextColor(it) }

            textMaxLines?.let { this@DefaultSnackBar.setTextMaxLines(it) }
        }
    }
}