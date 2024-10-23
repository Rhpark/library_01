package kr.open.rhpark.app.activity.recyclerview

import android.view.View
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.viewmodels.BaseViewModelEventFlow

class RecyclerviewActivityVm : BaseViewModelEventFlow<RecyclerviewActivityVmEvent>() {

    private val _fEditKey = MutableStateFlow("")

    init {
        viewModelScope.launch {

        }
    }

    fun onClickAdd(v: View) {
        val key = _fEditKey.value
        if(key.isNullOrEmpty()) {
            sendEvent(RecyclerviewActivityVmEvent.OnToastShow("key is null or Empty, key = $key"))
        } else {
            sendEvent(RecyclerviewActivityVmEvent.OnAddItem(key.toInt()))
        }
    }

    fun onClickRemove(v: View) {
        val key = _fEditKey.value
        if(key.isEmpty()) {
            sendEvent(RecyclerviewActivityVmEvent.OnToastShow("key is Empty, key = $key"))
        } else {
            sendEvent(RecyclerviewActivityVmEvent.OnRemoveItem(key.toInt()))
        }
    }

    fun updateText(key: CharSequence) {
        Logx.d("key $key")
        _fEditKey.value = key.toString()
    }
}