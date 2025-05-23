package kr.open.rhpark.library.util.extensions.context

import android.app.AlarmManager
import android.app.NotificationManager
import android.bluetooth.BluetoothManager
import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Build
import android.os.PowerManager
import android.os.Vibrator
import android.os.VibratorManager
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.telephony.euicc.EuiccManager
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kr.open.rhpark.library.domain.common.systemmanager.controller.SimpleNotificationController
import kr.open.rhpark.library.domain.common.systemmanager.controller.alarm.AlarmController
import kr.open.rhpark.library.domain.common.systemmanager.controller.SoftKeyboardController
import kr.open.rhpark.library.domain.common.systemmanager.controller.VibratorController
import kr.open.rhpark.library.domain.common.systemmanager.controller.WifiController
import kr.open.rhpark.library.domain.common.systemmanager.controller.windowmanager.FloatingViewController
import kr.open.rhpark.library.domain.common.systemmanager.info.battery.BatteryStateInfo
import kr.open.rhpark.library.domain.common.systemmanager.info.display.DisplayInfo
import kr.open.rhpark.library.domain.common.systemmanager.info.location.LocationStateInfo
import kr.open.rhpark.library.domain.common.systemmanager.info.network.NetworkStateInfo

/*****************
 * SystemService *
 *****************/

public fun Context.getWindowManager(): WindowManager = getSystemService(WindowManager::class.java)

public fun Context.getBatteryManager(): BatteryManager = getSystemService(BatteryManager::class.java)

public fun Context.getInputMethodManager(): InputMethodManager = getSystemService(InputMethodManager::class.java)

public fun Context.getTelephonyManager(): TelephonyManager = getSystemService(TelephonyManager::class.java)

public fun Context.getSystemNotificationManager(): NotificationManager = getSystemService(NotificationManager::class.java)

public fun Context.getSubscriptionManager(): SubscriptionManager = getSystemService(SubscriptionManager::class.java)

public fun Context.getEuiccManager(): EuiccManager = getSystemService(EuiccManager::class.java)

public fun Context.getConnectivityManager(): ConnectivityManager = getSystemService(ConnectivityManager::class.java)

public fun Context.getWifiManager(): WifiManager = getSystemService(WifiManager::class.java)

public fun Context.getLocationManager(): LocationManager = getSystemService(LocationManager::class.java)

public fun Context.getAlarmManager(): AlarmManager = getSystemService(AlarmManager::class.java)

public fun Context.getNotificationManager(): NotificationManager = getSystemService(NotificationManager::class.java)

public fun Context.getPowerManager(): PowerManager = getSystemService(PowerManager::class.java)

public fun Context.getBluetoothManager(): BluetoothManager = getSystemService(BluetoothManager::class.java)

/**
 * be used Build.VERSION.SDK_INT < Build.VERSION_CODES.S(31)
 */
public fun Context.getVibrator(): Vibrator = getSystemService(Vibrator::class.java)


/**
 * be used Build.VERSION.SDK_INT >= Build.VERSION_CODES.S(31)
 */
@RequiresApi(Build.VERSION_CODES.S)
public fun Context.getVibratorManager(): VibratorManager = getSystemService(VibratorManager::class.java)


/****************************
 * SystemService Controller *
 ****************************/

public fun Context.getSoftKeyboardController(): SoftKeyboardController = SoftKeyboardController(this)

public fun Context.getFloatingViewController(): FloatingViewController = FloatingViewController(this)

public fun Context.getAlarmController(): AlarmController = AlarmController(this)

public fun Context.getNotificationController(): SimpleNotificationController = SimpleNotificationController(this)

public fun Context.getDisplayInfo(): DisplayInfo = DisplayInfo(this)

public fun Context.getVibratorController(): VibratorController = VibratorController(this)

public fun Context.getWifiController(): WifiController = WifiController(this)

/*****************************
 * SystemService Access Info *
 *****************************/

public fun Context.getNetworkStateInfo(): NetworkStateInfo =
    NetworkStateInfo(this)

public fun Context.getBatteryStateInfo(): BatteryStateInfo =
    BatteryStateInfo(this)

public fun Context.getLocationStateInfo(coroutineScope: CoroutineScope): LocationStateInfo =
    LocationStateInfo(this, coroutineScope)


