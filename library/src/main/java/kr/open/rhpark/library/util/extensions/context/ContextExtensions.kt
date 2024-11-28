package kr.open.rhpark.library.util.extensions.context

import android.app.NotificationManager
import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.telephony.euicc.EuiccManager
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager


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
