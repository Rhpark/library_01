package kr.open.rhpark.library.system.service

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
import androidx.annotation.RequiresPermission
import kr.open.rhpark.library.system.service.info.battery.BatteryStateInfo
import kr.open.rhpark.library.system.service.info.display.DisplayInfo
import kr.open.rhpark.library.system.service.info.location.LocationStateInfo
import kr.open.rhpark.library.system.service.info.network.NetworkStateInfo
import kr.open.rhpark.library.system.service.controller.SoftKeyboardController
import kr.open.rhpark.library.system.service.controller.VibratorController
import kr.open.rhpark.library.system.service.controller.windowmanager.WindowManagerController
import kr.open.rhpark.library.util.extensions.context.getSystemBatteryManager
import kr.open.rhpark.library.util.extensions.context.getSystemConnectivityManager
import kr.open.rhpark.library.util.extensions.context.getSystemEuiccManager
import kr.open.rhpark.library.util.extensions.context.getSystemInputMethodManager
import kr.open.rhpark.library.util.extensions.context.getSystemLocationManager
import kr.open.rhpark.library.util.extensions.context.getSystemNotificationManager
import kr.open.rhpark.library.util.extensions.context.getSystemSubscriptionManager
import kr.open.rhpark.library.util.extensions.context.getSystemTelephonyManager
import kr.open.rhpark.library.util.extensions.context.getSystemWifiManager
import kr.open.rhpark.library.util.extensions.context.getSystemWindowManager

public class SystemServiceManager(context: Context) {

    /***************************
     *  System Service Manager *
     ***************************/
    public val inputMethodManager: InputMethodManager by lazy { context.getSystemInputMethodManager() }

    @get:RequiresPermission(android.Manifest.permission.BATTERY_STATS)
    public val batteryManager: BatteryManager by lazy { context.getSystemBatteryManager() }

    public val notificationManager: NotificationManager by lazy { context.getSystemNotificationManager() }

    public val windowManager: WindowManager by lazy { context.getSystemWindowManager() }

    @get:RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public val telephonyManager: TelephonyManager by lazy { context.getSystemTelephonyManager() }

    @get:RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public val subscriptionManager: SubscriptionManager by lazy { context.getSystemSubscriptionManager() }

    public val euiccManager: EuiccManager by lazy { context.getSystemEuiccManager() }

    public val connectivityManager: ConnectivityManager by lazy { context.getSystemConnectivityManager()    }

    public val wifiManager: WifiManager by lazy { context.getSystemWifiManager() }

    public val locationManager: LocationManager  by lazy { context.getSystemLocationManager()   }



    /*******************
     *  SystemManager  *
     *  Controller     *
     *******************/
    public val softKeyboardController: SoftKeyboardController by lazy { SoftKeyboardController(context, inputMethodManager) }

    public val vibratorController: VibratorController by lazy { VibratorController(context) }

    public val windowManagerController: WindowManagerController by lazy {
        WindowManagerController(context, windowManager)
    }



    /*******************
     *  SystemManager  *
     *  Access         *
     *******************/
    public val batteryInfo: BatteryStateInfo by lazy { BatteryStateInfo(context, batteryManager) }

    public val displayInfo: DisplayInfo by lazy { DisplayInfo(context, windowManager) }

    public val locationStateInfo : LocationStateInfo by lazy{ LocationStateInfo(context, locationManager) }

    public val networkInfo: NetworkStateInfo by lazy {
        NetworkStateInfo(context, telephonyManager, subscriptionManager, connectivityManager, wifiManager, euiccManager)
    }
}