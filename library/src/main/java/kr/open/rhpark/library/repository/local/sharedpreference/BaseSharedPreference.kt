package kr.open.rhpark.library.repository.local.sharedpreference

import android.content.Context
import android.content.SharedPreferences
import kr.open.rhpark.library.debug.logcat.Logx


public abstract class BaseSharedPreference(context: Context, groupKey: String, sharedPrivateMode: Int = Context.MODE_PRIVATE) {

    private val sp: SharedPreferences by lazy { context.getSharedPreferences(groupKey, sharedPrivateMode) }

    /******** Load data ********/
    protected fun load(key: String, defaultValue: String?): String? = sp.getString(key, defaultValue)
    protected fun load(key: String, defaultValue: Int): Int = sp.getInt(key, defaultValue)
    protected fun load(key: String, defaultValue: Float): Float = sp.getFloat(key, defaultValue)
    protected fun load(key: String, defaultValue: Boolean): Boolean = sp.getBoolean(key, defaultValue)
    protected fun load(key: String, defaultValue: Long): Long = sp.getLong(key, defaultValue)
    protected fun load(key: String, defaultValue: Set<String>?): Set<String>? = sp.getStringSet(key, defaultValue)

    /**
     * Save Data
     * must be called after apply() or commit()
     */
    private fun save(key: String, value: Any?) :SharedPreferences.Editor {
        val editor = sp.edit()
        return when (value) {
            is String -> editor.putString(key, value)
            is Boolean -> editor.putBoolean(key, value)
            is Float -> editor.putFloat(key, value)
            is Int -> editor.putInt(key, value)
            is Long -> editor.putLong(key, value)
            is Set<*> -> {
                try {
                    editor.putStringSet(key, value as Set<String>?)
                } catch (e: ClassCastException) {
                    Logx.e("ClassCastException: Set<*> is not Set<String>. Key: $key, Value: $value")
                    throw ClassCastException("ClassCastException: Set<*> is not Set<String>. Key: $key, Value: $value")
                }
            }
            else -> {
                if (value != null) {
                    Logx.e("Unsupported value type: ${value.javaClass}")
                }
                editor.remove(key)
            }
        }
    }

    protected fun saveApply(key: String, value: Any?) { save(key, value).apply() }

    protected fun saveCommit(key: String, value: Any?) { save(key, value).commit() }

    /******** remove data ********/

    /**
     * after editor.commit() or editor.apply() using sp.edit()
     */
    private fun removeAt(key: String): SharedPreferences.Editor = sp.edit().remove(key)

    protected fun removeAtApply(key: String) { removeAt(key).apply() }

    protected fun removeAtCommit(key: String) { removeAt(key).commit() }

    /**
     * after editor.commit() or editor.apply() using sp.edit()
     */
    private fun removeAll(): SharedPreferences.Editor = sp.edit().clear()

    public fun removeAllApply() { removeAll().apply() }

    public fun removeAllCommit() { removeAll().commit() }
}