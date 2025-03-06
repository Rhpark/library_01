package kr.open.rhpark.library.util.extensions.ui.view


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.IntegerRes
import kr.open.rhpark.library.util.extensions.context.getSoftKeyboardController

/********
 * View *
 ********/
public fun View.setVisible() {
    if (this.visibility != View.VISIBLE) this.visibility = View.VISIBLE
}

public fun View.setGone() {
    if (this.visibility != View.GONE) this.visibility = View.GONE
}

public fun View.setInvisible() {
    if (this.visibility != View.INVISIBLE) this.visibility = View.INVISIBLE
}

public fun View.setOnDebouncedClickListener(debounceTime: Long = 600L, action: (View) -> Unit) {
    var lastClickTime = 0L
    setOnClickListener { view ->
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime >= debounceTime) {
            lastClickTime = currentTime
            action(view)
        }
    }
}

/************
 * EditText *
 ************/
public fun EditText.showKeyBoard(flag: Int = InputMethodManager.SHOW_IMPLICIT): Boolean {
    return this.context.getSoftKeyboardController().show(this, flag)
}
public fun EditText.showKeyBoard(delay: Long, flag: Int = InputMethodManager.SHOW_IMPLICIT): Boolean {
    return this.context.getSoftKeyboardController().showDelay(this, delay, flag)
}

public fun EditText.hind(flag: Int = 0): Boolean {
    return this.context.getSoftKeyboardController().hide(this, flag)
}

public fun EditText.hindDelay(delay: Long, flag: Int = 0): Boolean {
    return this.context.getSoftKeyboardController().hideDelay(this, delay, flag)
}


/*************
 * ViewGroup *
 *************/
public fun ViewGroup.forEachChild(action: (View) -> Unit) {
    for (i in 0 until childCount) { action(getChildAt(i)) }
}

@SuppressLint("ResourceType")
public fun ViewGroup.getLayoutInflater(@IntegerRes xmlRes: Int, attachToRoot: Boolean): View =
    LayoutInflater.from(this.context).inflate(xmlRes, this, attachToRoot)

