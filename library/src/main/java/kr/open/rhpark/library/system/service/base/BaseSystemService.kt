package kr.open.rhpark.library.system.service.base

import android.content.Context
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.system.permission.PermissionCheck

/**
 * Base class for system services.
 * 시스템 서비스에 대한 기본 클래스.
 *
 *
 * @param context The application context.
 * @param context 애플리케이션 컨텍스트.
 *
 * @param permissionList The list of required permissions.
 * @param permissionList 필요한 권한 목록입니다.
 */
public abstract class BaseSystemService(protected val context: Context, permissionList: List<String>? = null) {

    private var isPermissionAllGranted = false
    private var deniedPermissionList = emptyList<String>()

    init {
        checkPermission(context, permissionList)
    }

    protected fun checkPermission(context: Context,permissionList: List<String>?){

        permissionList?.let { permissions->
            PermissionCheck(context, permissions).also {
                val deniedPermissionList = it.remainingPermission()
                if(deniedPermissionList.isEmpty()) {
                    setAllGranted()
                } else {
                    this.deniedPermissionList = deniedPermissionList.toList()
                }
            }
        } ?: setAllGranted()
    }

    protected fun setAllGranted() {
        isPermissionAllGranted = true
    }

    protected fun getDeniedPermissionList(): List<String> = deniedPermissionList

    /**
     * Checks if all permissions have been granted.
     * 모든 권한이 부여되었는지 확인.
     *
     * @return Returns true if all permissions have been granted, false otherwise.
     * @return 모든 권한이 부여된 경우 true, 그렇지 않으면 false를 반환.
     */
    protected fun isPermissionAllGranted() :Boolean{
        if(!isPermissionAllGranted) {
            Logx.d("Is not permission granted")
        }
        return isPermissionAllGranted
    }

    protected fun isPermissionDenied(vararg permission: String) :Boolean {
        permission.forEach { p->
            if(deniedPermissionList.contains(p)) return true
        }

        return false
    }

    public open fun onDestroy() {}
}