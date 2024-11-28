package kr.open.rhpark.library.system.service.info.location

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Build
import androidx.annotation.RequiresApi
import kr.open.rhpark.library.system.service.base.BaseSystemService

public class LocationStateInfo(
    context: Context,
    public val locationManager: LocationManager
) :
    BaseSystemService(context, listOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)) {

    private var onGpsChanged: ((isEnabled: Boolean) -> Unit)? = null

    /**
     * This is needed because of TelephonyCallback.CellInfoListener(Telephony.registerCallBack)
     * or
     * PhoneStateListener.LISTEN_CELL_INFO(Telephony.registerListen).
     */
    private var gpsStateBroadcastReceiver : BroadcastReceiver?=null

    private val intentFilter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)

    public fun registerGpsState(onGpsChanged: ((isEnabled: Boolean) -> Unit)? = null) {
        unregisterGpsState()
        this.onGpsChanged = onGpsChanged
        gpsStateBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action.equals(LocationManager.PROVIDERS_CHANGED_ACTION)) {
                    onGpsChanged?.let { it(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) }
                }
            }
        }
        context.registerReceiver(gpsStateBroadcastReceiver, intentFilter)
    }

    public fun unregisterGpsState() {
        gpsStateBroadcastReceiver?.let {
            context.unregisterReceiver(it)
        }
        gpsStateBroadcastReceiver = null
    }

    public fun isLocationEnabled(): Boolean =
        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

    public fun isGpsEnabled(): Boolean =
        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)


    public fun isNetworkEnabled(): Boolean =
        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)


    public fun isPassiveEnabled(): Boolean =
        locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)


    @RequiresApi(Build.VERSION_CODES.S)
    public fun isFusedEnabled(): Boolean =
        locationManager.isProviderEnabled(LocationManager.FUSED_PROVIDER)


    public fun isAnyEnabled(): Boolean {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (isLocationEnabled() || isGpsEnabled() || isNetworkEnabled() || isPassiveEnabled() || isFusedEnabled())
        } else {
            (isLocationEnabled() || isGpsEnabled() || isNetworkEnabled() || isPassiveEnabled())
        }
    }



}