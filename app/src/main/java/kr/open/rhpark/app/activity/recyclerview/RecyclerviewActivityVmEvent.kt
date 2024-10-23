package kr.open.rhpark.app.activity.recyclerview

sealed class RecyclerviewActivityVmEvent {
    data class OnAddItem(val key: Int) : RecyclerviewActivityVmEvent()
    data class OnRemoveItem(val key: Int) : RecyclerviewActivityVmEvent()
    data class OnToastShow(val msg: String) : RecyclerviewActivityVmEvent()
}