package kr.open.rhpark.library.util.extensions.ui.view

import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import kotlinx.coroutines.CoroutineScope
import kr.open.rhpark.library.util.extensions.context.getSoftKeyboardController

public fun EditText.getString(): String = this.text.toString()
public fun EditText.isTextEmpty(): Boolean = this.getString().isEmpty()

public fun EditText.textToInt(): Int? = this.text.toString().toIntOrNull()
public fun EditText.textToFloat(): Float? = this.text.toString().toFloatOrNull()
public fun EditText.textToDouble(): Double? = this.text.toString().toDoubleOrNull()

public fun EditText.showKeyBoard(flag: Int = InputMethodManager.SHOW_IMPLICIT): Boolean =
    this.context.getSoftKeyboardController().show(this, flag)

public fun EditText.showKeyBoard(delay: Long, flag: Int = InputMethodManager.SHOW_IMPLICIT): Boolean =
    this.context.getSoftKeyboardController().showDelay(this, delay, flag)

public fun EditText.showKeyBoard(delay: Long, coroutineScope: CoroutineScope,flag: Int = InputMethodManager.SHOW_IMPLICIT): Unit =
    this.context.getSoftKeyboardController().showDelay(this, delay, flag, coroutineScope)

public fun EditText.hind(flag: Int = 0): Boolean {
    return this.context.getSoftKeyboardController().hide(this, flag)
}

public fun EditText.hindDelay(delay: Long, flag: Int = 0): Boolean {
    return this.context.getSoftKeyboardController().hideDelay(this, delay, flag)
}