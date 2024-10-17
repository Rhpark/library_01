package kr.open.rhpark.library.debug.logx.data

import android.util.Log
import kr.open.rhpark.library.debug.logx.Logx

internal data class LogxStackTraceMetaData(private val item: StackTraceElement) {

    val fileName: String = item.fileName ?: Class.forName(item.className).simpleName.split("\$")[0]

    init {
        if (item.fileName == null) {
            Log.e(Logx.appName, "[ERROR] Can not read fileName!!!")
        }
    }

    private fun getFileLocation() = "(${fileName}:${item.lineNumber})"

    fun getMsgFrontNormal() = "${getFileLocation()}.${item.methodName} - "

    fun getMsgFrontParent() = "${getFileLocation()} - [${item.className}.${item.methodName}]"

    fun getMsgFrontJson() = "${getFileLocation()} - "
}