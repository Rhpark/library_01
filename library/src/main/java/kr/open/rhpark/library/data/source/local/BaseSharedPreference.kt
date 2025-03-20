package kr.open.rhpark.library.data.source.local

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kr.open.rhpark.library.debug.logcat.Logx


public abstract class BaseSharedPreference(context: Context, groupKey: String, sharedPrivateMode: Int = Context.MODE_PRIVATE) {

    protected val sp: SharedPreferences by lazy { context.applicationContext.getSharedPreferences(groupKey, sharedPrivateMode) }

    protected val commitMutex : Mutex by lazy { Mutex() } // use for coroutine commit, synchronized

    /******** Load data ********/
    protected fun getString(key: String, defaultValue: String?): String? = sp.getString(key, defaultValue)
    protected fun getInt(key: String, defaultValue: Int): Int = sp.getInt(key, defaultValue)
    protected fun getFloat(key: String, defaultValue: Float): Float = sp.getFloat(key, defaultValue)
    protected fun getBoolean(key: String, defaultValue: Boolean): Boolean = sp.getBoolean(key, defaultValue)
    protected fun getLong(key: String, defaultValue: Long): Long = sp.getLong(key, defaultValue)
    protected fun getSet(key: String, defaultValue: Set<String>?): Set<String>? = sp.getStringSet(key, defaultValue)
    protected fun getDouble(key: String, default:Double): Double  = sp.getString(key, null)?.toDoubleOrNull() ?: default

    /**
     * Save Data
     * must be called after apply() or commit()
     */
    protected fun SharedPreferences.Editor.putValue(key: String, value: Any?): SharedPreferences.Editor = when (value) {
        is String -> putString(key, value)
        is Boolean -> putBoolean(key, value)
        is Float-> putFloat(key, value)
        is Int -> putInt(key, value)
        is Double ->  putString(key, value.toString())
        is Long -> putLong(key, value)
        is Set<*> -> {
            val stringSet = value.filterIsInstance<String>().toSet()
            if (stringSet.size != value.size) {
                Logx.e("[ERROR] Set<*> is not Set<String>. Key: $key, Value: $value")
                throw ClassCastException("[ERROR] Set<*> is not Set<String>. Key: $key, Value: $value")
            }
            putStringSet(key, stringSet)
        }

        else -> {
            if (value != null) {
                Logx.e("Unsupported value type: $key, ${value.javaClass}")
            }
            remove(key)
        }
    }

    protected fun saveApply() { sp.edit().apply() }
    protected fun saveApply(key: String, value: Any?) { sp.edit().putValue(key, value).apply() }

    protected suspend inline fun commitDoWork(crossinline doWork: SharedPreferences.Editor.() -> Unit): Boolean =
        withContext(Dispatchers.IO) { commitMutex.withLock { sp.edit().apply { doWork() }.commit() } }

    protected suspend fun saveCommit():Boolean = commitDoWork{  }

    protected suspend fun saveCommit(key: String, value: Any?) :Boolean = commitDoWork{ putValue(key, value) }

    protected fun removeAtApply(key: String) { sp.edit().remove(key).apply() }

    protected suspend fun removeAtCommit(key: String):Boolean = commitDoWork{ remove(key) }

    protected fun removeAllApply() { sp.edit().clear().apply() }

    protected suspend fun removeAllCommit():Boolean = commitDoWork{ clear() }

    protected fun getEditor() : SharedPreferences.Editor = sp.edit()
}