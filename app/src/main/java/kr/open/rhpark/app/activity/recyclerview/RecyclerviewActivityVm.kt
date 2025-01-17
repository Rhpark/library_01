package kr.open.rhpark.app.activity.recyclerview

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kr.open.rhpark.app.activity.recyclerview.adapter.item.RcvItem
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.viewmodels.BaseViewModelEvent

class RecyclerviewActivityVm : BaseViewModelEvent<RecyclerviewActivityVmEvent>() {

    enum class AdapterType {
        ADAPTER,
        SIMPLE_ADAPTER,
        LIST_ADAPTER,
        LIST_SIMPLE_ADAPTER,
    }
    private var adapterType = AdapterType.ADAPTER

    private val _fEditKey = MutableStateFlow("")

    init {
        viewModelScope.launch {

        }
    }

    fun onClickAdd() {
        val key = _fEditKey.value
        if(key.isEmpty()) {
            sendEventVm(RecyclerviewActivityVmEvent.OnToastShow("key is null or Empty, key = $key"))
        } else if(key.toInt() == 0) {
            sendEventVm(RecyclerviewActivityVmEvent.OnToastShow("Over than 0"))
        } else {
            val dataList = getItemList(key.toInt())
            when(adapterType) {
                AdapterType.ADAPTER-> {
                    sendEventVm(RecyclerviewActivityVmEvent.OnSetAdapter(AdapterType.ADAPTER, dataList))
                }

                AdapterType.SIMPLE_ADAPTER -> {
                    sendEventVm(RecyclerviewActivityVmEvent.OnSetAdapter(AdapterType.SIMPLE_ADAPTER, dataList))
                }

                AdapterType.LIST_ADAPTER -> {
                    sendEventVm(RecyclerviewActivityVmEvent.OnSetAdapter(AdapterType.LIST_ADAPTER, dataList))
                }

                AdapterType.LIST_SIMPLE_ADAPTER -> {
                    sendEventVm(RecyclerviewActivityVmEvent.OnSetAdapter(AdapterType.LIST_SIMPLE_ADAPTER, dataList))
                }
            }
        }
    }

    private fun getItemList(size: Int) :List<RcvItem> {
        val dataList = ArrayList<RcvItem>()
        for (i in 0 until size) {
            dataList.add(RcvItem(i.toString(), "Test$i"))
        }
        return dataList.toList()
    }

    fun onClickRemove() {
        val key = _fEditKey.value
        if(key.isEmpty()) {
            sendEventVm(RecyclerviewActivityVmEvent.OnToastShow("key is Empty, key = $key"))
        } else {
            when(adapterType) {
                AdapterType.ADAPTER-> {
                    sendEventVm(RecyclerviewActivityVmEvent.OnRemoveItem(AdapterType.ADAPTER, key.toInt()))
                }

                AdapterType.SIMPLE_ADAPTER -> {
                    sendEventVm(RecyclerviewActivityVmEvent.OnRemoveItem(AdapterType.SIMPLE_ADAPTER, key.toInt()))
                }
                AdapterType.LIST_ADAPTER -> {
                    sendEventVm(RecyclerviewActivityVmEvent.OnRemoveItem(AdapterType.LIST_ADAPTER, key.toInt()))
                }

                AdapterType.LIST_SIMPLE_ADAPTER -> {
                    sendEventVm(RecyclerviewActivityVmEvent.OnRemoveItem(AdapterType.LIST_SIMPLE_ADAPTER, key.toInt()))
                }
            }
        }
    }

    fun updateText(key: CharSequence) {
        Logx.d("key $key")
        _fEditKey.value = key.toString()
    }

    fun onCheckRcvAdapter(isCheck:Boolean) {
        if(isCheck) {
            adapterType = AdapterType.ADAPTER
            sendEventVm(RecyclerviewActivityVmEvent.OnUpdateAdapter(AdapterType.ADAPTER))
        }
    }

    fun onCheckRcvSimpleAdapter(isCheck:Boolean) {
        if(isCheck) {
            adapterType = AdapterType.SIMPLE_ADAPTER
            sendEventVm(RecyclerviewActivityVmEvent.OnUpdateAdapter(AdapterType.SIMPLE_ADAPTER))
        }
    }

    fun onCheckRcvListAdapter(isCheck:Boolean) {
        if(isCheck) {
            adapterType = AdapterType.LIST_ADAPTER
            sendEventVm(RecyclerviewActivityVmEvent.OnUpdateAdapter(AdapterType.LIST_ADAPTER))
        }
    }

    fun onCheckRcvListSimpleAdapter(isCheck:Boolean) {
        if(isCheck) {
            adapterType = AdapterType.LIST_SIMPLE_ADAPTER
            sendEventVm(RecyclerviewActivityVmEvent.OnUpdateAdapter(AdapterType.LIST_SIMPLE_ADAPTER))
        }
    }
}