package kr.open.rhpark.app.fragment

sealed class FirstFragmentVmEvent {
    data class OnPermissionCheck(val permissionList: List<String>) : FirstFragmentVmEvent()
    data class OnShowSnackBar(val msg:String) : FirstFragmentVmEvent()
    data class OnShowToast(val msg:String) : FirstFragmentVmEvent()
}