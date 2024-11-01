package kr.open.rhpark.library.system.service.base

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import kr.open.rhpark.library.debug.logcat.Logx

public abstract class BaseSystemService(context: Context, permissionList: Array<String>? = null) {

    private var isPermissionGranted: Boolean = false
    private var deniedPermissionList: List<String> = emptyList()
    init {
        checkPermission(context, permissionList)
    }

    private fun checkPermission(context: Context, permissionList: Array<String>?){

        if (permissionList.isNullOrEmpty()) {
            isPermissionGranted = true
            return
        }
        deniedPermissionList =  permissionList.filter { permission ->
            ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
        }

        isPermissionGranted = deniedPermissionList.isEmpty()

        Logx.w("BaseSystemServiceInfo", "Permission is DENIED ${deniedPermissionList.toList()}")
    }

    public fun isPermissionGranted(): Boolean {
        if(!isPermissionGranted) {
            Logx.e("BaseSystemServiceInfo", "Permission is DENIED, $deniedPermissionList")
        }
        return isPermissionGranted
    }

    public fun getDeniedPermissionList(): List<String> = deniedPermissionList

    public open fun onDestroy() {}
}