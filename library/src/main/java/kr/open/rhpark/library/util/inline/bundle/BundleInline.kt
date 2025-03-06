package kr.open.rhpark.library.util.inline.bundle

import android.os.Bundle
import kr.open.rhpark.library.debug.logcat.Logx

public inline fun <reified T> Bundle.getValue(key: String, defaultValue: T): T {
    return if (containsKey(key)) {
        when (T::class) {
            Int::class -> getInt(key, defaultValue as Int) as T
            String::class -> getString(key, defaultValue as String) as T
            Boolean::class -> getBoolean(key, defaultValue as Boolean) as T
            Float::class -> getFloat(key, defaultValue as Float) as T
            Long::class -> getLong(key, defaultValue as Long) as T
            Double::class -> getDouble(key, defaultValue as Double) as T
            Byte::class -> getByte(key, defaultValue as Byte) as T
            ByteArray::class -> (getByteArray(key) ?: defaultValue as ByteArray) as T
            else -> {
                Logx.e("Can not cast Type ${T::class}")
                defaultValue
            }
        }
    } else {
        Logx.e("can not find key $key")
        defaultValue
    }
}