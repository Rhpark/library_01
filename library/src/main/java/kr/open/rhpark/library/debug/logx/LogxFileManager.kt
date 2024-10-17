package kr.open.rhpark.library.debug.logx

import android.util.Log
import kr.open.rhpark.library.debug.logx.vo.LogxType
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Required Permission file read / write
 */
internal class LogxFileManager(path:String) {

    private val fm = FileManager(path)
    private val logFileTitle = currentTimeFormatted() + "_Log.txt"

    fun addWriteLog(logType: LogxType, tag: String, msg: String) {
        fm.appendWriteFile(logFileTitle, "${currentTimeFormatted()}/${logType.logTypeString}/$tag : $msg")
    }

    private fun currentTimeFormatted():String =
        SimpleDateFormat("yy_MM_dd-HH_mm_ss").format(Date(System.currentTimeMillis()))


    private class FileManager(path: String) {

        val file = File(path)

        init {
            existsCheckAndMkdir()
        }

        private fun existsCheckAndMkdir() {
            if (file.exists()) {    return  }

            if (file.mkdir()) {
                Log.d(Logx.appName, "Logx Directory created! " + file.path)
            } else {
                Log.e(Logx.appName, "[Error] Failed to create Logx Directory! " + file.path)
            }
        }

        private fun mkFile(file: File) {
            if (file.exists()) {    return  }

            if(file.createNewFile()) {
                Log.d(Logx.appName , "Logx File created! " + file.getPath())
            } else {
                Log.e(Logx.appName , "[Error] Failed to created Logx File! " + file.getPath())
            }
        }

        fun appendWriteFile(title: String, msg: String) = writeFile(File("${file.path}/$title"),msg)

        private fun writeFile(file: File, msg: String) {

            mkFile(file)

            BufferedWriter(FileWriter(file, true)).use {
                it.write(msg)
                it.newLine()
            }
        }
    }
}