package kr.open.rhpark.app.activity.recyclerview

import android.view.View
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.open.rhpark.app.activity.recyclerview.adapter.RcvAdapter
import kr.open.rhpark.app.activity.recyclerview.adapter.RcvListAdapter
import kr.open.rhpark.app.activity.recyclerview.adapter.item.RcvItem
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.viewmodels.BaseViewModelEventFlow

class RecyclerviewActivityVm : BaseViewModelEventFlow<RecyclerviewActivityVmEvent>() {

    val ADAPTER = false
    val ADAPTER_STR = "Adapter Mode"
    val ADAPTERLIST = true
    val ADAPTERLIST_STR = "Adapter List Mode"

    private val adapter: RcvAdapter = RcvAdapter()
    private val listAdapter: RcvListAdapter = RcvListAdapter()

    private var adapterMode = ADAPTER


    private val _fEditKey = MutableStateFlow("")

    private val _fAdapterStr = MutableStateFlow(ADAPTER_STR)
    val fAdapterStr = _fAdapterStr.asStateFlow()

    init {
        viewModelScope.launch {

        }
    }

    fun onClickAdd(v: View) {
        val key = _fEditKey.value
        if(key.isNullOrEmpty()) {
            sendSharedFlowEvent(RecyclerviewActivityVmEvent.OnToastShow("key is null or Empty, key = $key"))
        } else if(key.toInt() == 0) {
            sendSharedFlowEvent(RecyclerviewActivityVmEvent.OnToastShow("Over than 0"))
        } else {
            val dataList = getItemList(key.toInt())
            if(adapterMode == ADAPTER) {
                adapter.addItems(dataList)
                sendSharedFlowEvent(RecyclerviewActivityVmEvent.OnUpdateAdapterMode(adapter))
            } else {
                listAdapter.addItems(dataList)
                sendSharedFlowEvent(RecyclerviewActivityVmEvent.OnUpdateListAdapterMode(listAdapter))
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

    fun onClickRemove(v: View) {
        val key = _fEditKey.value
        if(key.isEmpty()) {
            sendSharedFlowEvent(RecyclerviewActivityVmEvent.OnToastShow("key is Empty, key = $key"))
        } else {
            if(adapterMode == ADAPTER) {
                adapter.removeAt(key.toInt())
                sendSharedFlowEvent(RecyclerviewActivityVmEvent.OnUpdateAdapterMode(adapter))
            } else {
                listAdapter.removeAtItem(key.toInt())
                sendSharedFlowEvent(RecyclerviewActivityVmEvent.OnUpdateListAdapterMode(listAdapter))
            }
        }
    }

    fun updateText(key: CharSequence) {
        Logx.d("key $key")
        _fEditKey.value = key.toString()
    }

    fun change(isChecked:Boolean) {
        Logx.d(isChecked)

        if(isChecked) _fAdapterStr.value = ADAPTERLIST_STR
        else _fAdapterStr.value = ADAPTER_STR
        adapterMode = isChecked
        if(adapterMode == ADAPTER) {
            sendSharedFlowEvent(RecyclerviewActivityVmEvent.OnUpdateAdapterMode(adapter))
        } else {
            sendSharedFlowEvent(RecyclerviewActivityVmEvent.OnUpdateListAdapterMode(listAdapter))
        }
    }
}