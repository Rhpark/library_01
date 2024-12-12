package kr.open.rhpark.library.repository.local.sharedpreference

import android.content.Context
import android.content.SharedPreferences
import kr.open.rhpark.library.debug.logcat.Logx


public abstract class BaseSharedPreference(context: Context, groupKey: String, sharedPrivateMode: Int = Context.MODE_PRIVATE) {

    private val sp: SharedPreferences by lazy { context.getSharedPreferences(groupKey, sharedPrivateMode) }

    /******** Load data ********/
    protected fun getString(key: String, defaultValue: String?): String? = sp.getString(key, defaultValue)
    protected fun getInt(key: String, defaultValue: Int): Int = sp.getInt(key, defaultValue)
    protected fun getFloat(key: String, defaultValue: Float): Float = sp.getFloat(key, defaultValue)
    protected fun getBoolean(key: String, defaultValue: Boolean): Boolean = sp.getBoolean(key, defaultValue)
    protected fun getLong(key: String, defaultValue: Long): Long = sp.getLong(key, defaultValue)
    protected fun getSet(key: String, defaultValue: Set<String>?): Set<String>? = sp.getStringSet(key, defaultValue)

    protected fun getDouble(key: String, default:Double): Double {
        return sp.getString(key, null)?.let {
            try {
                it.toDouble()
            }catch (e:Exception) {
                e.printStackTrace()
                default
            }
        }?: default
    }

    /**
     * Save Data
     * must be called after apply() or commit()
     */
    protected fun put(key: String, value: Any?): SharedPreferences.Editor = when (value) {
        is String -> sp.edit().putString(key, value)
        is Boolean -> sp.edit().putBoolean(key, value)
        is Float-> sp.edit().putFloat(key, value)
        is Int -> sp.edit().putInt(key, value)
        is Double ->  sp.edit().putString(key, value.toString())
        is Long -> sp.edit().putLong(key, value)
        is Set<*> -> {
            try {
                sp.edit().putStringSet(key, value as Set<String?>)
            } catch (e: ClassCastException) {
                Logx.e("ClassCastException: Set<*> is not Set<String>. Key: $key, Value: $value")
                throw ClassCastException("ClassCastException: Set<*> is not Set<String>. Key: $key, Value: $value")
            }
        }

        else -> {
            if (value != null) {
                Logx.e("Unsupported value type: ${value.javaClass}")
            }
            sp.edit().remove(key)
        }
    }

    protected fun saveApply() { sp.edit().apply() }
    protected fun saveApply(key: String, value: Any?) { put(key, value).apply() }

    protected fun saveCommit() { sp.edit().commit() }
    protected fun saveCommit(key: String, value: Any?) { put(key, value).commit() }

    /**
     * remove data
     * after editor.commit() or editor.apply() using sp.edit()
     */
    private fun removeAt(key: String): SharedPreferences.Editor = sp.edit().remove(key)

    protected fun removeAtApply(key: String) { removeAt(key).apply() }

    protected fun removeAtCommit(key: String) { removeAt(key).commit() }

    /**
     * remove all
     * after editor.commit() or editor.apply() using sp.edit()
     */
    private fun removeAll(): SharedPreferences.Editor = sp.edit().clear()

    public fun removeAllApply() { removeAll().apply() }

    public fun removeAllCommit() { removeAll().commit() }
}