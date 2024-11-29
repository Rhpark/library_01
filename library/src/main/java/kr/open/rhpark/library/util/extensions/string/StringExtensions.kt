package kr.open.rhpark.library.util.extensions.string


public fun String.isEmailValid(): Boolean = android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
public fun String.isPhoneNumberValid(): Boolean = android.util.Patterns.PHONE.matcher(this).matches()
public fun String.isUrlValid(): Boolean = android.util.Patterns.WEB_URL.matcher(this).matches()
public fun String.isNumeric(): Boolean = matches(Regex("^[0-9]*$"))
public fun String.isAlphaNumeric(): Boolean = matches(Regex("^[a-zA-Z0-9]*$"))

public fun String.removeWhitespace(): String = replace("\\s".toRegex(),"")

public fun String.stripHtmlTags(): String = this.replace("<[^>]*>".toRegex(), "")