package kr.open.rhpark.library.system.service.base

import android.content.Context
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.system.permission.PermissionCheck

public abstract class BaseSystemService(context: Context, permissionList: List<String>? = null) {

    private var isPermissionAllGranted = false


    init {
        checkPermission(context, permissionList)
    }

    protected fun checkPermission(context: Context,permissionList: List<String>?){

        if(permissionList == null) {
            allGranted()
        } else {
            PermissionCheck(context, permissionList).also {
                val deniedPermissionList = it.remainingPermission()
                if(deniedPermissionList.isEmpty()) {
                    allGranted()
                } else {
                    deniedPermissionList(deniedPermissionList)
                }
            }
        }
    }

    private fun allGranted() {
        isPermissionAllGranted = true
    }

    protected fun deniedPermissionList(deniedPermissions: Array<String>) {
        Logx.d("deniedPermissions ${deniedPermissions.toList()}")
    }

    protected fun isPermissionAllGranted() :Boolean{
        if(!isPermissionAllGranted) {
            Logx.d("Is not permission granted")
        }
        return isPermissionAllGranted
    }

    public open fun onDestroy() {}
}