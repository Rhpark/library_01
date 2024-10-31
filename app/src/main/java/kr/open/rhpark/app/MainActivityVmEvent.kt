package kr.open.rhpark.app

sealed class MainActivityVmEvent {
    data class OnPermissionCheck(val permissionList: List<String>) : MainActivityVmEvent()
    data class OnShowSnackBar(val msg:String) : MainActivityVmEvent()
    data class OnShowToast(val msg:String) : MainActivityVmEvent()
    data class OnShowRecyclerviewActivity(val msg:String) : MainActivityVmEvent()
    data class OnShowVibratorActivity(val msg:String) : MainActivityVmEvent()
    data class OnShowUiUtilsActivity(val msg:String) : MainActivityVmEvent()
}