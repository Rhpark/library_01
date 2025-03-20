package kr.open.rhpark.library.ui.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import kr.open.rhpark.library.util.extensions.context.remainPermissions

/**
 * This class is used to check and request permissions in an Android app.
 * PermissionCheck 클래스는 Android 앱에서 권한을 확인하고 요청하는 데 사용.
 *
 * @param context  The application context.
 * @param requestCode  requestCode
 * @param permissions The list of permissions to request.
 * @param onResult  The callback to handle the permission request result.
 *
 * @param context 애플리케이션 컨텍스트.
 * @param requestCode  requestCode
 * @param permissions 요청할 권한 목록.
 * @param onResult 권한 요청 결과를 처리하는 콜백.
 *
 */
public class PermissionCheck(
    private val context: Context,
    public val requestCode: Int,
    permissions: List<String>,
    private val onResult: ((requestCode:Int, deniedPermissions: List<String>) -> Unit),
) {
    private var remainPermissions: List<String> = context.remainPermissions(permissions)

    /**
     * A companion object that defines a constant for the permission request code.
     * 권한 요청 코드에 대한 상수를 정의
     *
     * This constant can be used as the request code when calling the
     * `requestPermissions` method.
     * */


    public fun getRemainPermissions(): List<String> = remainPermissions

    public fun isRequestPermissionSystemAlertWindow(): Boolean = isRemainPermissionSystemAlertWindow() && !Settings.canDrawOverlays(context)

    private fun isRemainPermissionSystemAlertWindow(): Boolean = remainPermissions.contains(Manifest.permission.SYSTEM_ALERT_WINDOW)

    /**
     * Returns an Intent to request the SYSTEM_ALERT_WINDOW permission.
     * 시스템 알림 창 권한을 요청하는 Intent를 반환.
     *
     * @param packageName application package name
     * @param packageName 애플리케이션의 패키지 이름.
     *
     * @return 시스템 알림 창 권한을 요청하는 Intent입니다.
     */
    public fun requestPermissionAlertWindow(packageName: String): Intent =
        Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))



    /**
     * Handles the result of a permission request.
     * 권한 요청 결과를 처리.
     */
    public fun resultPermissionsFromActivity(permissions: Array<out String>, grantResults: IntArray) {
        val deniedList = mutableListOf<String>()
        permissions.forEachIndexed { index, permission ->
            if (grantResults[index] == PackageManager.PERMISSION_DENIED) {
                if(permission == Manifest.permission.SYSTEM_ALERT_WINDOW) {
                    if(!Settings.canDrawOverlays(context)) {
                        deniedList.add(permission)
                    }
                } else {
                    deniedList.add(permission)
                }
            }
        }
        result(deniedList)
    }

    public fun resultPermissionsFromFragment(permissions: Map<String,Boolean>) {
        val deniedList = permissions.filter { !it.value }.map { it.key }.toMutableList()
        result(deniedList)
    }

    private fun result(deniedList: MutableList<String>) {
        if(isRemainPermissionSystemAlertWindow()) {
            if(!Settings.canDrawOverlays(context)) deniedList.add(Manifest.permission.SYSTEM_ALERT_WINDOW)
        }
        onResult(requestCode, deniedList)
    }
}