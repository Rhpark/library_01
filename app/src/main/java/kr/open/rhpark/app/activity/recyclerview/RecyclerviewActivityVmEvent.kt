package kr.open.rhpark.app.activity.recyclerview

import kr.open.rhpark.app.activity.recyclerview.adapter.item.RcvItem

sealed class RecyclerviewActivityVmEvent {
    data class OnSetAdapter(val adapterType: RecyclerviewActivityVm.AdapterType, val datas: List<RcvItem>) : RecyclerviewActivityVmEvent()

    data class OnUpdateAdapter(val adapterType: RecyclerviewActivityVm.AdapterType) : RecyclerviewActivityVmEvent()

    data class OnRemoveItem(val adapterType: RecyclerviewActivityVm.AdapterType, val position:Int) : RecyclerviewActivityVmEvent()

    data class OnToastShow(val msg: String) : RecyclerviewActivityVmEvent()
}