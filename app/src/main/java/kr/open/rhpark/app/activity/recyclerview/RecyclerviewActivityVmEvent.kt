package kr.open.rhpark.app.activity.recyclerview

import kr.open.rhpark.app.activity.recyclerview.adapter.RcvAdapter
import kr.open.rhpark.app.activity.recyclerview.adapter.RcvListAdapter

sealed class RecyclerviewActivityVmEvent {
    data class OnToastShow(val msg: String) : RecyclerviewActivityVmEvent()
    data class OnUpdateAdapterMode(val adapter: RcvAdapter) : RecyclerviewActivityVmEvent()
    data class OnUpdateListAdapterMode(val listAdapter: RcvListAdapter) : RecyclerviewActivityVmEvent()
}