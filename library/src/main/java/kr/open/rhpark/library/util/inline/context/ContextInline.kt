package kr.open.rhpark.library.util.inline.context

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import android.os.Bundle
import android.provider.Settings
import androidx.core.content.ContextCompat

public inline fun Context.startActivity(activity: Class<*>, extras: Bundle? = null, intentFlags: IntArray? = null) {
    val intent = Intent(this, activity).apply {
        extras?.let { putExtras(it) }
        intentFlags?.let { it.forEach { item->addFlags(item) } }
    }
    startActivity(intent)
}


/********************
 * Permission Check *
 ********************/

public inline fun Context.hasPermission(permission: String): Boolean =
    when (permission) {
        Manifest.permission.SYSTEM_ALERT_WINDOW -> Settings.canDrawOverlays(this)
        else -> {
            if(getPermissionProtectionLevel(permission) == PermissionInfo.PROTECTION_DANGEROUS) {
                ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
//            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

public inline fun Context.hasPermissions(vararg permissions: String): Boolean =
    permissions.all { permission -> hasPermission(permission) }

public inline fun Context.remainPermissions(permissions: List<String>): List<String> =
    permissions.filterNot { hasPermission(it) }


public inline fun Context.getPermissionProtectionLevel(permission: String): Int = try {
    packageManager.getPermissionInfo(permission, 0).protection
} catch (e: Exception) {
//    e.printStackTrace()
    PermissionInfo.PROTECTION_DANGEROUS
}