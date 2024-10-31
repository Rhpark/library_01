package kr.open.rhpark.library.system.service.base

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import kr.open.rhpark.library.debug.logcat.Logx

public abstract class BaseSystemService(context: Context, permissionList: Array<String>? = null) {

    private var isPermissionGranted: Boolean = false

    init {
        checkPermission(context, permissionList)
    }

    private fun checkPermission(context: Context, permissionList: Array<String>?){

        if (permissionList.isNullOrEmpty()) {
            isPermissionGranted = true
            return
        }
        val deniedPermissionList =  permissionList.filter { permission ->
            ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
        }
        if (deniedPermissionList.isEmpty()) {
            isPermissionGranted = true
            return
        }
        isPermissionGranted = false
        Logx.e("BaseSystemServiceInfo", "Permission is DENIED ${deniedPermissionList.toList()}")
    }

    public fun isPermissionGranted(): Boolean = isPermissionGranted

    public open fun onDestroy() {}
}