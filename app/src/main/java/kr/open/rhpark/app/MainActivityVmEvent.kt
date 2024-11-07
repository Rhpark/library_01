package kr.open.rhpark.app

sealed class MainActivityVmEvent {
    data class OnPermissionCheck(val permissionList: List<String>) : MainActivityVmEvent()

    data class OnShowActivity(val activity: Class<*>) : MainActivityVmEvent()
}