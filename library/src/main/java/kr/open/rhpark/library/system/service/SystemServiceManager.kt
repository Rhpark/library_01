package kr.open.rhpark.library.system.service

import android.app.NotificationManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.telephony.euicc.EuiccManager
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import kr.open.rhpark.library.system.service.access.BatteryStateInfo
import kr.open.rhpark.library.system.service.controller.SoftKeyboardController
import kr.open.rhpark.library.system.service.controller.VibratorController
import kr.open.rhpark.library.system.service.controller.WindowManagerController

public class SystemServiceManager(context: Context) {

    /***************************
     *  System Service Manager *
     ***************************/
    public val inputMethodManager: InputMethodManager by lazy {
        context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    public val batteryManager: BatteryManager by lazy {
        context.getSystemService(AppCompatActivity.BATTERY_SERVICE) as BatteryManager
    }

    public val notificationManager: NotificationManager by lazy {
        context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
    }

    public val windowManager: WindowManager by lazy {
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    public val telephonyManager: TelephonyManager by lazy {
        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }

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



    /*******************
     *  SystemManager  *
     *  Controller     *
     *******************/
    public val softKeyboardController: SoftKeyboardController by lazy {
        SoftKeyboardController(context, inputMethodManager)
    }

    public val vibratorController: VibratorController by lazy { VibratorController(context) }

    public val windowController: WindowManagerController by lazy {
        WindowManagerController(context, windowManager)
    }


    /*******************
     *  SystemManager  *
     *  Access         *
     *******************/
    public val batteryInfo: BatteryStateInfo by lazy {
        BatteryStateInfo(context, batteryManager)
    }
}