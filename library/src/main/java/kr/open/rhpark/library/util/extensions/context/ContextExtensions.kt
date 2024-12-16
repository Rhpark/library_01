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
import kotlinx.coroutines.CoroutineScope
import kr.open.rhpark.library.system.service.controller.SoftKeyboardController
import kr.open.rhpark.library.system.service.controller.VibratorController
import kr.open.rhpark.library.system.service.controller.windowmanager.FloatingViewController
import kr.open.rhpark.library.system.service.info.battery.BatteryStateInfo
import kr.open.rhpark.library.system.service.info.display.DisplayInfo
import kr.open.rhpark.library.system.service.info.location.LocationStateInfo
import kr.open.rhpark.library.system.service.info.network.NetworkStateInfo

/*****************
 * SystemService *
 *****************/

public fun Context.getSystemWindowManager(): WindowManager =
    getSystemService(WindowManager::class.java)

public fun Context.getSystemBatteryManager(): BatteryManager =
    getSystemService(BatteryManager::class.java)

public fun Context.getSystemInputMethodManager(): InputMethodManager =
    getSystemService(InputMethodManager::class.java)

public fun Context.getSystemTelephonyManager(): TelephonyManager =
    getSystemService(TelephonyManager::class.java)

public fun Context.getSystemNotificationManager(): NotificationManager =
    getSystemService(NotificationManager::class.java)

public fun Context.getSystemSubscriptionManager(): SubscriptionManager =
    getSystemService(SubscriptionManager::class.java)

public fun Context.getSystemEuiccManager(): EuiccManager =
    getSystemService(EuiccManager::class.java)

public fun Context.getSystemConnectivityManager(): ConnectivityManager =
    getSystemService(ConnectivityManager::class.java)

public fun Context.getSystemWifiManager(): WifiManager =
    getSystemService(WifiManager::class.java)

public fun Context.getSystemLocationManager(): LocationManager =
    getSystemService(LocationManager::class.java)



/****************************
 * SystemService Controller *
 ****************************/

public fun Context.getSoftKeyboardController(): SoftKeyboardController =
    SoftKeyboardController(this, getSystemInputMethodManager())

public fun Context.getVibratorController():VibratorController = VibratorController(this)

public fun Context.getFloatingViewControllerController(): FloatingViewController =
    FloatingViewController(this, getSystemWindowManager())



/*****************************
 * SystemService Access Info *
 *****************************/


public fun Context.getNetworkStateInfo():NetworkStateInfo =
    NetworkStateInfo(this,
        getSystemTelephonyManager(), getSystemSubscriptionManager(),
        getSystemConnectivityManager(), getSystemWifiManager(), getSystemEuiccManager())

public fun Context.getDisplayInfo(): DisplayInfo = DisplayInfo(this, getSystemWindowManager())

public fun Context.getBatteryStateInfo(coroutineScope: CoroutineScope): BatteryStateInfo =
    BatteryStateInfo(this, getSystemBatteryManager(), coroutineScope)

public fun Context.getLocationStateInfo(coroutineScope: CoroutineScope): LocationStateInfo =
    LocationStateInfo(this, getSystemLocationManager(), coroutineScope)


/********************
 * Permission Check *
 ********************/

public fun Context.hasPermission(permission: String): Boolean =
    when (permission) {
        Manifest.permission.SYSTEM_ALERT_WINDOW -> Settings.canDrawOverlays(this)
        else -> ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

public fun Context.hasPermissions(vararg permissions: String): Boolean =
    permissions.all { permission -> hasPermission(permission) }

public fun Context.remainPermissions(permissions: List<String>): List<String> =
    permissions.filterNot { hasPermission(it) }
