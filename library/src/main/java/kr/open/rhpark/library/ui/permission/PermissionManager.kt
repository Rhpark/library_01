package kr.open.rhpark.library.ui.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import kr.open.rhpark.library.util.extensions.context.remainPermissions


public open class PermissionManager() {

    private var onResult: ((deniedPermissions: List<String>) -> Unit)? = null

    public fun getIntentForSystemAlertWindow(context: Context): Intent =
        Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${context.packageName}")
        )

    private fun isRemainPermissionSystemAlertWindow(permissions: List<String>): Boolean =
        permissions.contains(Manifest.permission.SYSTEM_ALERT_WINDOW)

    public fun isRequestPermissionSystemAlertWindow(
        context: Context,
        permissions: List<String>
    ): Boolean =
        isRemainPermissionSystemAlertWindow(permissions) && !Settings.canDrawOverlays(context)

    public fun result(context: Context, permissions: Map<String, Boolean>) {
        val deniedPermissions = mutableListOf<String>()
        permissions.forEach { (permission, granted) ->
            if (!granted) {
                if (permission == Manifest.permission.SYSTEM_ALERT_WINDOW) {
                    if (!Settings.canDrawOverlays(context)) {
                        deniedPermissions.add(permission)
                    }
                } else {
                    deniedPermissions.add(permission)
                }
            }
        }
        onResult?.invoke(deniedPermissions)
        onResult = null
    }

    public fun request(
        context: Context,
        requestPermissionLauncher: ActivityResultLauncher<Array<String>>,
        requestPermissionAlertWindowLauncher: ActivityResultLauncher<Intent>,
        permissions: List<String>,
        onResult: ((deniedPermissions: List<String>) -> Unit)
    ) {
        val remainingPermissions = context.remainPermissions(permissions)
        if (remainingPermissions.isNotEmpty()) {
            if (isRequestPermissionSystemAlertWindow(context, remainingPermissions)) {
                requestPermissionAlertWindowLauncher.launch(
                    getIntentForSystemAlertWindow(context)
                )
            }
            requestPermissionLauncher.launch(remainingPermissions.toTypedArray())
            this.onResult = onResult
        } else {
            onResult(emptyList())
        }
    }
}