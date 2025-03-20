package kr.open.rhpark.app.activity.alarm

import android.content.Context
import android.content.SharedPreferences.Editor
import android.net.Uri
import kr.open.rhpark.library.data.source.local.BaseSharedPreference
import kr.open.rhpark.library.domain.common.systemmanager.controller.alarm.dto.AlarmDTO

class AlarmSharedPreference(context: Context): BaseSharedPreference(context,"Alarm") {

    private val key = "Key"
    private val msg = "Msg"
    private val hour = "Hour"
    private val min = "Hour"
    private val sec = "sec"
    private val title = "title"
    private val acquireTime = "acquireTime"
    private val isActive = "isActive"
    private val sound = "sound"
    private val longArray = "longArray"
    private val longArraySize = "longArraySize"
    private val isAllowIdle = "isAllowIdle"
    private fun putAlarm(alarmDTO: AlarmDTO): Editor = getEditor().apply {
        this.putValue(key, alarmDTO.key)
        this.putValue(msg, alarmDTO.msg)
        this.putValue(hour, alarmDTO.hour)
        this.putValue(min, alarmDTO.minute)
        this.putValue(sec, alarmDTO.second)
        this.putValue(title, alarmDTO.title)
        this.putValue(acquireTime, alarmDTO.acquireTime)
        this.putValue(isActive, alarmDTO.isActive)
        this.putValue(isAllowIdle, alarmDTO.isAllowIdle)
        this.putValue(sound, alarmDTO.sound.toString())
        alarmDTO.vibrationEffect?.let { item->
            val size = item.size
            this.putValue(longArraySize, size)
            for (i in 0 until size) {
                this.putValue(longArray + i, item.get(i))
            }
        }
    }

    fun saveAlarm(alarmDTO: AlarmDTO) {
        putAlarm(alarmDTO).apply()
    }

    fun loadAlarm():AlarmDTO {
        val vibrationEffectSize = getInt(longArraySize,-1)
        val mt = mutableListOf<Long>()
        if (vibrationEffectSize > 0) {
            for (i in 0 until vibrationEffectSize) {
                mt.add(getLong(longArray+i,-1))
            }
        }
        return AlarmDTO(key = getInt(key,-1),
            title = getString(title,"title")!!,
            msg = getString(msg,"msg")!!,
            isActive = getBoolean(isActive,false),
            isAllowIdle = getBoolean(isAllowIdle,false),
            sound = getString(sound, null)?.let { Uri.parse(it) } ?: null,
            hour =  getInt(hour,-1),
            minute =  getInt(min,-1),
            second =  getInt(sec,-1),
            acquireTime = getLong(acquireTime,-1),
            vibrationEffect = if(vibrationEffectSize> 0 ) mt.toLongArray() else null
        )
    }
}