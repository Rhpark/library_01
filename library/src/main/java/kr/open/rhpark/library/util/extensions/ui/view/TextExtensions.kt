package kr.open.rhpark.library.util.extensions.ui.view

import android.widget.TextView


public fun TextView.getString(): String = this.text.toString()
public fun TextView.isTextEmpty(): Boolean = this.getString().isEmpty()
public fun TextView.isTextNullOrEmpty(): Boolean = this.getString().isNullOrEmpty()

public fun TextView.textToInt(): Int? = this.text.toString().toIntOrNull()
public fun TextView.textToFloat(): Float? = this.text.toString().toFloatOrNull()
public fun TextView.textToDouble(): Double? = this.text.toString().toDoubleOrNull()



