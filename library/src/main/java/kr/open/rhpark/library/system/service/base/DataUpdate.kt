package kr.open.rhpark.library.system.service.base

public class DataUpdate<TYPE>(
    private var typeOrigin: TYPE,
    private var updateListener: ((res: TYPE) -> Unit)? = null
) {

    init {
        updateListener?.let { it(typeOrigin) }
    }

    public fun update(typeNew: TYPE) {
        if (typeOrigin != typeNew) {
            updateListener?.let { it(typeNew) }
        }
        typeOrigin = typeNew
    }

    public fun updateListener(updateListener: ((res: TYPE) -> Unit)? = null) {
        this.updateListener = updateListener
        updateListener?.let { it(typeOrigin) }
    }
}