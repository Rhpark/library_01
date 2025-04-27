package kr.open.rhpark.library.debug.logcat.data

import android.util.Log
import kr.open.rhpark.library.debug.logcat.Logx

internal data class LogxStackTraceMetaData(private val item: StackTraceElement) {

    // 파일 이름을 한 번만 계산하고 저장
    val fileName: String by lazy {
        try {
            item.fileName ?: Class.forName(item.className).simpleName.split("\$")[0]
        } catch (e: ClassNotFoundException) {
            Log.e(Logx.appName, "[ERROR] Failed to resolve className: ${item.className}", e)
            "Unknown"
        } catch (e: Exception) {
            Log.e(Logx.appName, "[ERROR] Unexpected error getting fileName: ${e.message}", e)
            "Unknown"
        }
    }

    // 위치 정보 캐싱
    private val fileLocation by lazy { "(${fileName}:${item.lineNumber})" }

    // 일반 메시지 앞부분 캐싱
    private val msgFrontNormalCache by lazy {   "${fileLocation}.${item.methodName} - " }

    // 부모 메시지 앞부분 캐싱
    private val msgFrontParentCache by lazy { "${fileLocation} - [${item.className}.${item.methodName}]" }

    // JSON 메시지 앞부분 캐싱
    private val msgFrontJsonCache by lazy { "${fileLocation} - " }

    fun getMsgFrontNormal(): String = msgFrontNormalCache

    fun getMsgFrontParent(): String = msgFrontParentCache

    fun getMsgFrontJson(): String = msgFrontJsonCache
}