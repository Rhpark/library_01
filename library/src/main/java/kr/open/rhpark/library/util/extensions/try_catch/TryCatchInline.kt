package kr.open.rhpark.library.util.extensions.try_catch

import kr.open.rhpark.library.debug.logcat.Logx

public inline fun <T> safeCatch(
    operation: String,
    defaultValue: T,
    block: () -> T
): T {
    return try {
        block()
    } catch (e: Exception) {
        e.printStackTrace()
        Logx.e("$operation: ${e.message}")
        defaultValue
    }
}