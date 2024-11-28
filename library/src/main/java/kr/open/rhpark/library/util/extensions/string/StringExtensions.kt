package kr.open.rhpark.library.util.extensions.string

import java.util.Locale

public fun String.isEmailValid(): Boolean = android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

public fun String.removeWhitespace(): String = replace("\\s".toRegex(),"")

//public fun String.capitalizeWords(): String =
//    split(" ").map { it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } }.joinToString