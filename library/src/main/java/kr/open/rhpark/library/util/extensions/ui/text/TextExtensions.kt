package kr.open.rhpark.library.util.extensions.ui.text

import android.widget.EditText
import android.widget.TextView

public fun EditText.getString(): String = this.text.toString()
public fun EditText.isTextEmpty(): Boolean = this.getString().isEmpty()

public fun TextView.getString(): String = this.text.toString()
public fun TextView.isTextEmpty(): Boolean = this.getString().isEmpty()

public fun TextView.toInt(): Int? = this.text.toString().toIntOrNull()
public fun TextView.toFloat(): Float? = this.text.toString().toFloatOrNull()
public fun TextView.toDouble(): Double? = this.text.toString().toDoubleOrNull()

public fun EditText.toInt(): Int? = this.text.toString().toIntOrNull()
public fun EditText.toFloat(): Float? = this.text.toString().toFloatOrNull()
public fun EditText.toDouble(): Double? = this.text.toString().toDoubleOrNull()


