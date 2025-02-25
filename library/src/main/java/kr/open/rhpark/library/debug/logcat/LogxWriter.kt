package kr.open.rhpark.library.debug.logcat

import android.util.Log
import kr.open.rhpark.library.debug.logcat.data.LogxStackTrace
import kr.open.rhpark.library.debug.logcat.vo.LogxType


internal class LogxWriter {

    private val logxStackTrace = LogxStackTrace()
    private val logSaver:LogxFileManager by lazy { LogxFileManager(Logx.saveFilePath) }

    fun writeExtensions(tag: String, msg: Any?, type: LogxType) {
        if (!isDebug(type)) { return }
        try { log(filterExtensionsType(tag, type), msg, type) }
        catch (e: IndexOutOfBoundsException) { e.printStackTrace() }
    }

    fun write(tag: String, msg: Any?, type: LogxType) {
        if (!isDebug(type)) { return }
        try { log(filter(tag, type), msg, type) }
        catch (e: IndexOutOfBoundsException) { e.printStackTrace() }
    }

    fun writeThreadId(tag: String, msg: Any?) {
        val type = LogxType.THREAD_ID
        if (!isDebug(type)) { return }
        try { log(threadIdType(filter(tag, type)), msg, type) }
        catch (e: IndexOutOfBoundsException) { e.printStackTrace() }
    }

    private fun threadIdType(pair: Pair<String, String?>?): Pair<String, String?>? =
        if (pair == null)  null
        else Pair(pair.first,"[${Thread.currentThread().id}]${pair.second}")

    fun writeParent(tag: String, msg: Any?) {
        val type = LogxType.PARENT
        if (!isDebug(type)) { return }
        try {log(parentType(filter(tag, type)), msg, type)}
        catch (e: IndexOutOfBoundsException) { e.printStackTrace() }
    }

    fun writeExtensionsParent(tag: String, msg: Any?) {
        val type = LogxType.PARENT
        if (!isDebug(type)) { return }
        try {log(parentExtensionsType(filterExtensionsType(tag, type)), msg, type)}
        catch (e: IndexOutOfBoundsException) { e.printStackTrace() }
    }

    private fun parentType(pair: Pair<String, String?>?): Pair<String, String?>? = logxStackTrace.getParentStackTrace().let {
        if (pair == null) return null
        log(Pair(pair.first,"┎${it.getMsgFrontParent()}"), "", LogxType.PARENT)
        return Pair(pair.first, "┖${pair.second}")
    }

    private fun parentExtensionsType(pair: Pair<String, String?>?): Pair<String, String?>? = logxStackTrace.getParentExtensionsStackTrace().let {
        if (pair == null) return null
        log(Pair(pair.first,"┎${it.getMsgFrontParent()}"), "", LogxType.PARENT)
        return Pair(pair.first, "┖${pair.second}")
    }

    fun writeJsonExtensions(tag: String, msg: String) {
        if (!isDebug(LogxType.JSON)) { return }
        try {
            filterExtensionsType(tag, LogxType.JSON)?.let {
                val jsonTag = jsonType(it)
                log(jsonTag, "=========JSON_START========", LogxType.JSON)
                jsonMsgSort(it.first, "${msg}")
                log(jsonTag, "=========JSON_END==========", LogxType.JSON)
            }
        } catch (e: IndexOutOfBoundsException) { e.printStackTrace() }
    }

    fun writeJson(tag: String, msg: String) {
        if (!isDebug(LogxType.JSON)) { return }
        try {
            filter(tag, LogxType.JSON)?.let {
                val jsonTag = jsonType(it)
                log(jsonTag, "=========JSON_START========", LogxType.JSON)
                jsonMsgSort(it.first, msg)
                log(jsonTag, "=========JSON_END==========", LogxType.JSON)
            }
        } catch (e: IndexOutOfBoundsException) { e.printStackTrace() }
    }

    private fun jsonType(pair: Pair<String, String?>): Pair<String, String?> =
        Pair(pair.first, "${pair.second}")

    private fun log(pair: Pair<String, String?>?, msg: Any?, logType: LogxType) {

        if(pair == null) {  return  }
        val logTag = pair.first
        val logMsg = "${pair.second}$msg"
        when (logType) {
            LogxType.VERBOSE ->     Log.v(logTag, logMsg)
            LogxType.INFO ->        Log.i(logTag, logMsg)
            LogxType.JSON ->        Log.i(logTag, logMsg)
            LogxType.DEBUG ->       Log.d(logTag, logMsg)
            LogxType.THREAD_ID ->   Log.d(logTag, logMsg)
            LogxType.PARENT ->      Log.d(logTag, logMsg)
            LogxType.WARN ->        Log.w(logTag, logMsg)
            LogxType.ERROR ->       Log.e(logTag, logMsg)
        }

        if (Logx.isDebugSave) { logSaver.addWriteLog(logType, logTag, logMsg) }
    }


    private fun getTypeToString(typeRes:LogxType) :String = when(typeRes) {
        LogxType.THREAD_ID -> " [T_ID] :"
        LogxType.PARENT -> " [PARENT] :"
        LogxType.JSON -> " [JSON] :"
        else -> " :"
    }

    private fun filterExtensionsType(tag:String, type: LogxType):Pair<String,String>? = logxStackTrace.getExtensionsStackTrace().let {

        if(!isLogFilter(tag, it.fileName.split(".")[0])) return null
        return Pair("${Logx.appName} [$tag]${getTypeToString(type)}", it.getMsgFrontNormal())
    }

    /**
     * stackTrace ex)
     * it.className -> include Package (ex a.b.c.MainActivity)
     * it.fileName -> MainActivity.kt
     * return ex) TAG : RhPark [tag] : , (MainActivity.kt:50).onCreate
     */
    private fun filter(tag:String, type: LogxType):Pair<String,String>? = logxStackTrace.getStackTrace().let {

        if(!isLogFilter(tag, it.fileName.split(".")[0])) return null
        return Pair("${Logx.appName} [$tag]${getTypeToString(type)}", it.getMsgFrontNormal())
    }

    private fun jsonMsgSort(tag:String, msg: String) {

        val result = StringBuilder()
        var indentLevel = 0
        var inQuotes = false

        for (char in msg) {
            when (char) {
                '{', '[' -> {
                    result.append(char)
                    if (!inQuotes) {
                        result.append("\n")
                        indentLevel++
                        result.append("  ".repeat(indentLevel))
                    }
                }
                '}', ']' -> {
                    if (!inQuotes) {
                        result.append("\n")
                        indentLevel = maxOf(0, indentLevel - 1)
                        result.append("  ".repeat(indentLevel))
                    }
                    result.append(char)
                }
                ',' -> {
                    result.append(char)
                    if (!inQuotes) {
                        result.append("\n")
                        result.append("  ".repeat(indentLevel))
                    }
                }
                '"' -> {
                    result.append(char)
                    if (result.lastOrNull() != '\\') {
                        inQuotes = !inQuotes // 따옴표 안인지 여부를 반전
                    }
                }
                else -> result.append(char)
            }
        }
        log(Pair(tag, ""), result, LogxType.JSON)
    }

    private fun isDebug(logType: LogxType) = if (!Logx.isDebug) false
    else Logx.debugLogTypeList.contains(logType)

    private fun isDebugFilter(logTag: String) = if (!Logx.isDebugFilter) true
    else Logx.debugFilterList.contains(logTag)

    private fun isLogFilter(tag: String, fileName: String): Boolean =
        if (tag.isNotEmpty() && (!isDebugFilter(tag) && !isDebugFilter(fileName))) {
            false
        } else !(tag.isEmpty() && !isDebugFilter(fileName))
}