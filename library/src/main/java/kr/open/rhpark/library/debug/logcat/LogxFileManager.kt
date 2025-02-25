package kr.open.rhpark.library.debug.logcat

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kr.open.rhpark.library.debug.logcat.vo.LogxType
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Required Permission file read / write
 */
internal class LogxFileManager(path:String) {

    private val fm : FileManager by lazy { FileManager(path) }
    private val logFileTitle = currentTimeFormatted() + "_Log.txt"

    fun addWriteLog(logType: LogxType, tag: String, msg: String) {
        fm.appendWriteFile(logFileTitle, "${currentTimeFormatted()}/${logType.logTypeString}/$tag : $msg")
    }

    private fun currentTimeFormatted():String =
        SimpleDateFormat("yy-MM-dd, HH:mm:ss.SSS",Locale.US).format(Date(System.currentTimeMillis()))


    private class FileManager(path: String) {

        val file = File(path)

        private companion object {
            private val logWriterScope = CoroutineScope(Dispatchers.IO + Job()) // Singleton
        }

        init {
            existsCheckAndMkdir()
            finishCheck()
        }

        private fun existsCheckAndMkdir() {
            if (file.exists()) {    return  }

            if (file.mkdirs()) {
                Log.d(Logx.appName, "Logx Directory created! " + file.path)
            } else {
                Log.e(Logx.appName, "[Error] Failed to create Logx Directory! " + file.path)
            }
        }

        private fun mkFile(file: File) {
            if (file.exists()) {    return  }
            try {
                if(file.createNewFile()) {
                    Log.d(Logx.appName , "Logx File created! " + file.getPath())
                } else {
                    Log.e(Logx.appName , "[Error] Failed to created Logx File! " + file.getPath())
                }
            } catch (e:IOException) {
                Log.e(Logx.appName, "[Exception] Failed to create file: ${file.path}", e)
            }
        }

        fun appendWriteFile(title: String, msg: String) = writeFile(File("${file.path}/$title"),msg)

        private fun writeFile(file: File, msg: String) {

            logWriterScope.launch {
                mkFile(file)
                BufferedWriter(FileWriter(file, true)).use {
                    it.write(msg)
                    it.newLine()
                    it.flush()
                }
            }
        }
        private fun finishCheck() {
            // 앱 종료 시 로그 저장
            Runtime.getRuntime().addShutdownHook(Thread {
                shutdownLogger()
            })

            // 크래시 발생 시 로그 저장
            Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
                shutdownLogger()
                throwable.printStackTrace()
            }
        }

        private fun shutdownLogger() {
            logWriterScope.cancel()
        }
    }
}