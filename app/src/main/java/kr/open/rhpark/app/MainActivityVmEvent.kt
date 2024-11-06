package kr.open.rhpark.app

sealed class MainActivityVmEvent {
    data class OnPermissionCheck(val permissionList: List<String>) : MainActivityVmEvent()
    data class OnShowRecyclerviewActivity(val msg:String) : MainActivityVmEvent()
    data class OnShowVibratorActivity(val msg:String) : MainActivityVmEvent()
    data class OnShowUiUtilsActivity(val msg:String) : MainActivityVmEvent()
    data class OnShowToastSnackBar(val msg:String) : MainActivityVmEvent()
    data class OnShowFragmentActivity(val msg:String) : MainActivityVmEvent()
    data class OnDisplayActivity(val msg:String) : MainActivityVmEvent()
}