package kr.open.rhpark.library.debug.logx

import kr.open.rhpark.library.debug.logx.vo.LogxType


/**
 * How to use
 *
 * Code input ex)
 * Logx.d(),Logx.d(msg),Logx.d(tag, msg)
 *
 * Logcat output ex)
 * D/AppName [tag] : (FileName:NumberLine).Method - msg
 *
 * Logx.p()...  Log.i + Parent method call name check
 * Logx.j(msg),Logx.j(tag,msg) Log.v + JSON code parsing
 * Logx.t()... Log d + Current Thread Id
 */
public object Logx {

    private const val DEFAULT_TAG = ""
    private const val DEFAULT_MSG = ""

    public var isDebug: Boolean = true // true -> show logcat, else is gone
    public var isDebugFilter: Boolean = false // do verification debugTagCheckList
    public var isDebugSave: Boolean = false // Log write currentTime.txt file (required storage read/write permission)
    public var saveFilePath: String = "/sdcard/"

    internal var appName = "RhPark"

    private val logWriter = LogxWriter()

    internal var debugTagCheckList = listOf<String>()

    internal var debugLogTypeList = listOf(
        LogxType.VERBOSE,
        LogxType.DEBUG,
        LogxType.INFO,
        LogxType.WARN,
        LogxType.ERROR,
        LogxType.PARENT,
        LogxType.JSON,
        LogxType.THREAD_ID,
    )

    /**
     * setting LogxType(DebugType) list
     * default is All LogTypeList
     * Only added LogxType list are printed
     *
     * logTypeList 리스트 설정
     * 기본값 모두 추가됨
     * 추가 된 것들만 출력됨
     * @param logTypeList : LogxType List
     */
    public fun setDebugLogTypeList(logTypeList:List<LogxType>) { debugLogTypeList = logTypeList.toList() }

    /**
     * setting Debug Tag list
     * isDebugFilter true is Check only tagList
     * @param tagList : LogxType List
     */
    public fun setDebugTagCheckList(tagList:List<String>) { debugTagCheckList = tagList.toList() }

    public fun setAppName(appName:String) {    this.appName = appName  }

    /** ex) Log.v AppName [] : (FileName:LineNumber).Method - **/
    @JvmStatic
    public fun v() { logWriter.write(DEFAULT_TAG, DEFAULT_MSG, LogxType.VERBOSE) }

    /** ex) Log.v AppName [] : (FileName:LineNumber).Method - @param msg **/
    @JvmStatic
    public fun v(msg: Any?) { logWriter.write(DEFAULT_TAG, msg , LogxType.VERBOSE) }

    /** ex) Log.v AppName [tag] : (FileName:LineNumber).Method - @param msg **/
    @JvmStatic
    public fun v(tag: String, msg: Any?) { logWriter.write(tag, msg, LogxType.VERBOSE) }


    /** ex) Log.d AppName [] : (FileName:LineNumber).Method - **/
    @JvmStatic
    public fun d() { logWriter.write(DEFAULT_TAG, DEFAULT_MSG, LogxType.DEBUG) }

    /** ex) Log.d AppName [] : (FileName:LineNumber).Method - msg **/
    @JvmStatic
    public fun d(msg: Any?) { logWriter.write(DEFAULT_TAG, msg, LogxType.DEBUG) }

    /** ex) Log.d AppName [tag] : (FileName:LineNumber).Method - msg **/
    @JvmStatic
    public fun d(tag: String, msg: Any?) { logWriter.write(tag, msg, LogxType.DEBUG) }


    /** ex) Log.i AppName [] : (FileName:LineNumber).Method - **/
    @JvmStatic
    public fun i() { logWriter.write(DEFAULT_TAG, DEFAULT_MSG, LogxType.INFO) }

    /** ex) Log.i AppName [] : (FileName:LineNumber).Method - msg **/
    @JvmStatic
    public fun i(msg: Any?) { logWriter.write(DEFAULT_TAG, msg, LogxType.INFO)}

    /** ex) Log.i AppName [tag] : (FileName:LineNumber).Method - msg **/
    @JvmStatic
    public fun i(tag: String, msg: Any?) { logWriter.write(tag, msg, LogxType.INFO) }


    /** ex) Log.w AppName [] : (FileName:LineNumber).Method - **/
    @JvmStatic
    public fun w() { logWriter.write(DEFAULT_TAG, DEFAULT_TAG, LogxType.WARN) }

    /** ex) Log.w AppName [] : (FileName:LineNumber).Method - msg **/
    @JvmStatic
    public fun w(msg: Any?) { logWriter.write(DEFAULT_TAG, msg, LogxType.WARN) }

    /** ex) Log.w AppName [tag] : (FileName:LineNumber).Method - msg **/
    @JvmStatic
    public fun w(tag: String, msg: Any?) { logWriter.write(tag, msg, LogxType.WARN) }


    /** ex) Log.e AppName [] : (FileName:LineNumber).Method - **/
    @JvmStatic
    public fun e() { logWriter.write(DEFAULT_TAG, DEFAULT_MSG, LogxType.ERROR) }

    /** ex) Log.e AppName [] : (FileName:LineNumber).Method - msg **/
    @JvmStatic
    public fun e(msg: Any?) { logWriter.write(DEFAULT_TAG, msg, LogxType.ERROR) }

    /** ex) Log.d AppName [tag] : (FileName:LineNumber).Method - msg **/
    @JvmStatic
    public fun e(tag: String, msg: Any?) { logWriter.write(tag, msg, LogxType.ERROR) }


    /**
     * Log.i + Parent call method name
     * ex) Log.i AppName [] [PARENT] : ┎(ParentFileName:ParentLineNumber) -[ClassPath.Method]
     *     Log.i AppName [] [PARENT] : ┖(FileName:LineNumber).Method -
     */
    @JvmStatic
    public fun p() { logWriter.writeParent(DEFAULT_TAG, DEFAULT_MSG) }

    /**
     * Log.i + Parent call method name
     * ex) Log.i AppName [] [PARENT] : ┎(ParentFileName:ParentLineNumber) -[ClassPath.Method]
     *     Log.i AppName [] [PARENT] : ┖(FileName:LineNumber).Method - msg
     */
    @JvmStatic
    public fun p(msg: Any?) { logWriter.writeParent(DEFAULT_TAG, msg) }

    /**
     * Log.i + Parent call method name
     * ex) Log.i AppName [tag] [PARENT] : ┎(ParentFileName:ParentLineNumber) -[ClassPath.Method]
     *     Log.i AppName [tag] [PARENT] : ┖(FileName:LineNumber).Method - msg
     */
    @JvmStatic
    public fun p(tag: String, msg: Any?) { logWriter.writeParent(tag, msg) }


    /**
     * Log.d + Current Thread Id
     * ex) Log.d AppName [] [T_ID] : [CurrentThread.id](FileName:LineNumber).Method -
     */
    @JvmStatic
    public fun t() { logWriter.writeThreadId(DEFAULT_TAG, DEFAULT_MSG) }

    /**
     * Log.d + Current Thread Id
     * ex) Log.d AppName [] [T_ID] : [CurrentThread.id](FileName:LineNumber).Method - msg
     */
    @JvmStatic
    public fun t(msg: Any?) { logWriter.writeThreadId(DEFAULT_TAG, msg) }

    /**
     * Log.d + Current Thread Id
     * ex) Log.d AppName [tag] [T_ID] : [CurrentThread.id](FileName:LineNumber).Method - msg
     */
    @JvmStatic
    public fun t(tag: String, msg: Any?) { logWriter.writeThreadId(tag, msg) }


    /**
     * Log.v + Json Parse
     * ex) Log.d AppName [] [JSON] : (FileName:LineNumber).Method - ====Json Start====
     *     Log.d AppName [] [JSON] : (FileName:LineNumber). Json Format msg ...
     *     Log.d AppName [] [JSON] : (FileName:LineNumber). Json Format msg ...
     *     Log.d AppName [] [JSON] : (FileName:LineNumber).Method - ====JsonEnd====
     */
    @JvmStatic
    public fun j(msg: String) { logWriter.writeJson(DEFAULT_TAG, msg) }

    /**
     * Log.v + Json Parse
     * ex) Log.d AppName [tag] [JSON] : (FileName:LineNumber).Method - ====Json Start====
     *     Log.d AppName [tag] [JSON] : (FileName:LineNumber). Json Format msg ...
     *     Log.d AppName [tag] [JSON] : (FileName:LineNumber). Json Format msg ...
     *     Log.d AppName [tag] [JSON] : (FileName:LineNumber).Method - ====JsonEnd====
     */
    @JvmStatic
    public fun j(tag: String, msg: String) { logWriter.writeJson(tag, msg) }
}