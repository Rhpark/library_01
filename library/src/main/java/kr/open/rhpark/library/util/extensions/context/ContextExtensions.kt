package kr.open.rhpark.library.util.extensions.context

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.provider.Settings
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.telephony.euicc.EuiccManager
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat


public fun Context.getSystemWindowManager(): WindowManager =
    getSystemService(Context.WINDOW_SERVICE) as WindowManager

public fun Context.getSystemBatteryManager(): BatteryManager =
    getSystemService(Context.BATTERY_SERVICE) as BatteryManager

public fun Context.getSystemInputMethodManager(): InputMethodManager =
    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

public fun Context.getSystemTelephonyManager(): TelephonyManager =
    getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

public fun Context.getSystemNotificationManager(): NotificationManager =
    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

public fun Context.getSystemSubscriptionManager(): SubscriptionManager =
    getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager

public fun Context.getSystemEuiccManager(): EuiccManager =
    getSystemService(Context.EUICC_SERVICE) as EuiccManager

public fun Context.getSystemConnectivityManager(): ConnectivityManager =
    getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

public fun Context.getSystemWifiManager(): WifiManager =
    getSystemService(Context.WIFI_SERVICE) as WifiManager

public fun Context.getSystemLocationManager(): LocationManager =
    getSystemService(Context.LOCATION_SERVICE) as LocationManager


public fun Context.hasPermissions(vararg permissions:String):Boolean {

    permissions.forEach { permission ->
        if (permission == Manifest.permission.SYSTEM_ALERT_WINDOW) {
            if (!Settings.canDrawOverlays(this)) return false
        } else if (ContextCompat.checkSelfPermission(this, permission) ==
            PackageManager.PERMISSION_DENIED) {
            return false
        }
    }
    return true
}

public fun Context.remainPermissions(permissions: List<String>): List<String> = permissions.filter { permission ->
    if (permission == Manifest.permission.SYSTEM_ALERT_WINDOW) {
        !Settings.canDrawOverlays(this)
    } else {
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED
    }
}
