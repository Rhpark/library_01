package kr.open.rhpark.library.system.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat

public class PermissionCheck(
    private val context: Context,
    private val permissions: List<String>,
    public val onPermissionResult:(grantedPermissions: List<String>, deniedPermissions: List<String>) ->Unit,
) {

    /**
     * A companion object that defines a constant for the permission request code.
     *
     * This constant can be used as the request code when calling the
     * `requestPermissions` method.
     * */
    public companion object {
        public const val PERMISSION_REQUEST_CODE: Int = 1001
    }

    private var isAllGranted = false

    private var remainRequestPermissionList :Array<String>  = remainingPermission()

    init {
        if (remainRequestPermissionList.isEmpty()) {
            onPermissionResult(permissions, emptyList())
            isAllGranted = true
        } else {
            isAllGranted = false
        }
    }

    public fun isRequestAllGranted(isRequestedPermissionGranted: Boolean): Boolean {
        return if(isRequestedPermissionGranted) {
            if(isContainsSystemAlertWindow()) {
                if(Settings.canDrawOverlays(context)) true
                else false
            } else true
        } else false
    }

    public fun isAllGranted(): Boolean = isAllGranted

    public fun getRemainRequestPermissionList(): Array<String> =
        remainRequestPermissionList.filter { it != Manifest.permission.SYSTEM_ALERT_WINDOW }.toTypedArray()

    public fun remainingPermission(): Array<String> = permissions.filter { permission->
        if(isContainsSystemAlertWindow()) {
            !Settings.canDrawOverlays(context)
        } else {
            ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
        }
    }.toTypedArray()

    public fun isRequestPermissionSystemAlertWindow(): Boolean =
        (isContainsSystemAlertWindow() && !Settings.canDrawOverlays(context))

    public fun requestPermissionAlertWindow(packageName: String): Intent =
        Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))

    private fun isContainsSystemAlertWindow(): Boolean = permissions.contains(Manifest.permission.SYSTEM_ALERT_WINDOW)

    public fun result(grantedPermissions: MutableList<String>, deniedPermissions: MutableList<String>) {

        if(isContainsSystemAlertWindow()) {
            if(Settings.canDrawOverlays(context)) {
                grantedPermissions.add(Manifest.permission.SYSTEM_ALERT_WINDOW)
            } else {
                deniedPermissions.add(Manifest.permission.SYSTEM_ALERT_WINDOW)
            }
        }
        onPermissionResult(grantedPermissions,deniedPermissions)
    }
}