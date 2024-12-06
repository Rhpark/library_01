package kr.open.rhpark.library.debug.logcat.data

import android.util.Log
import kr.open.rhpark.library.debug.logcat.Logx

internal class LogxStackTrace {

    private val LOG_EXTENTIONS_PARENT_STACK_LEVEL = 9
    private val LOG_PARENT_STACK_LEVEL = 8
    private val LOG_EXTEPTIONS_STACK_LEVEL = 8
    private val LOG_NORMAL_STACK_LEVEL = 7

    fun getParentStackTrace() = getStackTrace(LOG_PARENT_STACK_LEVEL)
    fun getParentExtensionsStackTrace() = getStackTrace(LOG_EXTENTIONS_PARENT_STACK_LEVEL)
    fun getExtensionsStackTrace() = getStackTrace(LOG_EXTEPTIONS_STACK_LEVEL)
    fun getStackTrace() = getStackTrace(LOG_NORMAL_STACK_LEVEL)

    private fun getStackTrace(level: Int): LogxStackTraceMetaData {

        val stackTraceSize = Thread.currentThread().stackTrace.size

        if(level >= stackTraceSize) {
            Log.e(Logx.appName, "[Error] IndexOutOfBoundsException!! MinState $level stackTraceSize $stackTraceSize!!")
            throw IndexOutOfBoundsException("MinState $level stackTraceSize $stackTraceSize!!")
        }

        var isCoroutine = false

        for (i in level until stackTraceSize) {

//            Log.d("Test", "isNormalMethod index $i, class Name ${Thread.currentThread().stackTrace[i].className}, ${Thread.currentThread().stackTrace[i].fileName}, ${Thread.currentThread().stackTrace[i].methodName}, ${Thread.currentThread().stackTrace[i].lineNumber}")
            val item = Thread.currentThread().stackTrace[i]
            if (!isNormalMethod(item)) {
//                Log.d("Test","continue isNormalMethod index $i, class Name ${item.className}, ${item.fileName}, ${item.methodName}, ${item.lineNumber}")
                continue
            }

            if (isCoroutinePath(item.className)) {
                isCoroutine = true
//                Log.d("Test","continue isCoroutinePath index $i, class Name ${item.className}, ${item.fileName}")
                continue
            }
            if (!isCoroutine) {
                return LogxStackTraceMetaData(item)
            } else {
                isCoroutine = false
            }
        }

        val defaultItem = Thread.currentThread().stackTrace[level]

        Log.w(Logx.appName, "[Warning] Can not find class !!!, " + defaultItem.className + ", " + defaultItem.methodName )

        return LogxStackTraceMetaData(defaultItem)
    }

    private fun isCoroutinePath(className: String): Boolean = (
            className.startsWith("kotlin.coroutines") ||
                    className.startsWith("kotlinx.coroutines")
            )

    private fun isNormalMethod(item: StackTraceElement): Boolean = !(
            item.methodName.contains("access$") ||
//                    item.methodName.contains("lambda$") ||
//                    item.className.contains("SyntheticClass") ||
                    item.className.contains("Lambda0") ||
                    item.className.contains("Lambda$")
            )
}