package kr.open.rhpark.library.system.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat

/**
 * This class is used to check and request permissions in an Android app.
 * PermissionCheck 클래스는 Android 앱에서 권한을 확인하고 요청하는 데 사용.
 *
 * @param context  The application context.
 * @param permissions The list of permissions to request.
 * @param onPermissionResult  The callback to handle the permission request result.
 *
 * @param context 애플리케이션 컨텍스트.
 * @param permissions 요청할 권한 목록.
 * @param onPermissionResult 권한 요청 결과를 처리하는 콜백.
 *
 */
public class PermissionCheck(
    private val context: Context,
    private val permissions: List<String>,
    public val onPermissionResult: ((grantedPermissions: List<String>, deniedPermissions: List<String>) -> Unit)? = null,
) {

    /**
     * A companion object that defines a constant for the permission request code.
     * 권한 요청 코드에 대한 상수를 정의
     *
     * This constant can be used as the request code when calling the
     * `requestPermissions` method.
     * */
    public companion object {
        public const val PERMISSION_REQUEST_CODE: Int = 1001
    }


    /**
     * A variable that indicates whether all permissions have been granted.
     *
     * 모든 권한이 부여되었는지 여부를 나타내는 변수.
     */
    private var isAllGranted = false


    /**
     * A list of permissions that have not yet been requested.
     *
     * 아직 요청되지 않은 권한 목록.
     */
    private var remainRequestPermissionList :Array<String>  = remainingPermission(permissions)

    init {
        if (remainRequestPermissionList.isEmpty()) {

            /*
             * If all permissions have already been granted, call the callback.
             *
             * 모든 권한이 이미 부여된 경우 콜백을 호출.
             */
            onPermissionResult?.let { it(permissions, emptyList()) }

            isAllGranted = true
        } else {
            isAllGranted = false
        }
    }

    /**
     * Checks if all requested permissions have been granted.
     * 요청된 권한이 모두 부여되었는지 확인.
     *
     * @param isRequestedPermissionGranted Indicates whether the requested permission has been granted.
     * @param isRequestedPermissionGranted 요청된 권한이 부여되었는지 여부를 나타내는 변수.
     *
     * @return Returns true if all permissions have been granted, else is false.
     * @return 모든 권한이 부여된 경우 true, 그렇지 않으면 false를 반환.
     */
    public fun isRequestAllGranted(isRequestedPermissionGranted: Boolean): Boolean {
        return if (isRequestedPermissionGranted) {
            if (isContainsSystemAlertWindow()) {
                if (Settings.canDrawOverlays(context)) true
                else false
            } else true
        } else false
    }


    /**
     * Checks if all permissions have been granted.
     * 모든 권한이 부여되었는지 확인.
     *
     * @return true is all permissions have been granted, else is false.
     * @return 모든 권한이 부여된 경우 true, 그렇지 않으면 false를 반환.
     */
    public fun isAllGranted(): Boolean = isAllGranted


    /**
     * Returns a list of permissions thathave not yet been requested, excluding the SYSTEM_ALERT_WINDOW permission.
     * 아직 요청되지 않은 권한 목록을 반환. 시스템 알림 창 권한(SYSTEM_ALERT_WINDOW)은 제외.
     *
     * @return The list of permissions that have not yet been requested, excluding SYSTEM_ALERT_WINDOW.
     * @return 아직 요청되지 않은 권한 목록입니다.
     */
    public fun getRemainRequestPermissionList(): Array<String> =
        remainRequestPermissionList.filter { it != Manifest.permission.SYSTEM_ALERT_WINDOW }.toTypedArray()


    /**
     * Gets a list of permissions that have not yet been requested.
     * 아직 요청되지 않은 권한 목록을 가져옴.
     *
     * @return remain request permission list.
     * @return 아직 요청되지 않은 권한 목록.
     */
    public fun remainingPermission(permissions: List<String>): Array<String> = permissions.filter { permission->
        if(isContainsSystemAlertWindow()) {
            !Settings.canDrawOverlays(context)
        } else {
            ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
        }
    }.toTypedArray()


    /**
     * Checks if the SYSTEM_ALERT_WINDOW permission has been requested.
     * 시스템 알림 창 권한이 요청되었는지 확인.
     *
     * @return true is SYSTEM_ALERT_WINDOW permission has been requested, else is false.
     * @return 시스템 알림 창 권한이 요청된 경우 true, 그렇지 않으면 false를 반환.
     */
    public fun isRequestPermissionSystemAlertWindow(): Boolean =
        (isContainsSystemAlertWindow() && !Settings.canDrawOverlays(context))


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
     * Checks if the permission list contains the SYSTEM_ALERT_WINDOW permission.
     * 권한 목록에 시스템 알림 창 권한이 포함되어 있는지 확인.
     *
     * @return 권한 목록에 시스템 알림 창 권한이 포함되어 있는 경우 true, 그렇지 않으면 false를 반환합니다.
     */
    private fun isContainsSystemAlertWindow(): Boolean = permissions.contains(Manifest.permission.SYSTEM_ALERT_WINDOW)

    /**
     * Handles the result of a permission request.
     * 권한 요청 결과를 처리.
     *
     * @param grantedPermissions 부여된 권한 목록.
     * @param deniedPermissions 거부된 권한 목록.
     */
    public fun result(grantedPermissions: MutableList<String>, deniedPermissions: MutableList<String>) {

        if(isContainsSystemAlertWindow()) {
            if(Settings.canDrawOverlays(context)) {
                grantedPermissions.add(Manifest.permission.SYSTEM_ALERT_WINDOW)
            } else {
                deniedPermissions.add(Manifest.permission.SYSTEM_ALERT_WINDOW)
            }
        }
        onPermissionResult?.let { it(grantedPermissions,deniedPermissions) }
    }
}