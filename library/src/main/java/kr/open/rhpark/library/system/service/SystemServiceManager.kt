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
import androidx.appcompat.app.AppCompatActivity
import kr.open.rhpark.library.system.service.access.battery.BatteryStateInfo
import kr.open.rhpark.library.system.service.access.display.DisplayInfo
import kr.open.rhpark.library.system.service.access.internet.telephony.TelephonyStateInfo
import kr.open.rhpark.library.system.service.access.internet.network.NetworkStateInfo
import kr.open.rhpark.library.system.service.access.internet.telephony.LocationStateInfo
import kr.open.rhpark.library.system.service.access.internet.usim.UsimStateInfo
import kr.open.rhpark.library.system.service.controller.SoftKeyboardController
import kr.open.rhpark.library.system.service.controller.VibratorController
import kr.open.rhpark.library.system.service.controller.windowmanager.WindowManagerController

public class SystemServiceManager(context: Context) {

    /***************************
     *  System Service Manager *
     ***************************/
    public val inputMethodManager: InputMethodManager by lazy {
        context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    @get:RequiresPermission(android.Manifest.permission.BATTERY_STATS)
    public val batteryManager: BatteryManager by lazy {
        context.getSystemService(AppCompatActivity.BATTERY_SERVICE) as BatteryManager
    }

    public val notificationManager: NotificationManager by lazy {
        context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
    }

    public val windowManager: WindowManager by lazy {
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    @get:RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public val telephonyManager: TelephonyManager by lazy {
        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }

    @get:RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public val subscriptionManager: SubscriptionManager by lazy {
        context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
    }

    public val euiccManager: EuiccManager by lazy {
        context.getSystemService(Context.EUICC_SERVICE) as EuiccManager
    }

    public val connectivityManager: ConnectivityManager by lazy {
        context.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    public val wifiManager: WifiManager by lazy {
        context.getSystemService(AppCompatActivity.WIFI_SERVICE) as WifiManager
    }

    public val locationManager: LocationManager  by lazy {
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }



    /*******************
     *  SystemManager  *
     *  Controller     *
     *******************/
    public val softKeyboardController: SoftKeyboardController by lazy {
        SoftKeyboardController(context, inputMethodManager)
    }

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

    public val telephonyStateInfo: TelephonyStateInfo by lazy {
        TelephonyStateInfo(context, telephonyManager, subscriptionManager)
    }

    public val usimStateInfo: UsimStateInfo by lazy {
        UsimStateInfo(context, telephonyManager, subscriptionManager, euiccManager)
    }

    public val locationStateInfo : LocationStateInfo by lazy{ LocationStateInfo(context, locationManager) }


    public val networkInfo: NetworkStateInfo by lazy {
        NetworkStateInfo(context, telephonyManager, subscriptionManager, connectivityManager, wifiManager)
    }
}