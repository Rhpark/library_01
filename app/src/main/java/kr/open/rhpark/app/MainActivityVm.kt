package kr.open.rhpark.app

import android.Manifest
import kr.open.rhpark.app.activity.battery.BatteryActivity
import kr.open.rhpark.app.activity.display.DisplayActivity
import kr.open.rhpark.app.activity.location.LocationActivity
import kr.open.rhpark.app.activity.network.NetworkActivity
import kr.open.rhpark.app.activity.notification.NotificationActivity
import kr.open.rhpark.app.activity.recyclerview.RecyclerViewActivity
import kr.open.rhpark.app.activity.second.FragmentShowActivity
import kr.open.rhpark.app.activity.toast_snackbar.ToastSnackBarActivity
import kr.open.rhpark.app.activity.vibrator.VibratorActivity
import kr.open.rhpark.app.activity.wifi.WifiActivity
import kr.open.rhpark.app.activity.window.WindowActivity
import kr.open.rhpark.library.ui.viewmodels.BaseViewModelEvent

class MainActivityVm : BaseViewModelEvent<MainActivityVmEvent>() {

    fun onClickPermission() {
        sendEventVm(
            MainActivityVmEvent.OnPermissionCheck(
                listOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_PHONE_NUMBERS,
                    Manifest.permission.SYSTEM_ALERT_WINDOW
                )
            )
        )
    }

    private fun showActivity(activity: Class<*>) {
        sendEventVm(MainActivityVmEvent.OnShowActivity(activity))
    }

    fun onClickNetworkActivity() {
        showActivity(NetworkActivity::class.java)
    }

    fun onClickToastSnackBar() {
        showActivity(ToastSnackBarActivity::class.java)
    }

    fun onClickShowRecyclerviewActivity() {
        showActivity(RecyclerViewActivity::class.java)
    }

    fun onClickShowVibratorActivity() {
        showActivity(VibratorActivity::class.java)
    }

    fun onClickShowWindowActivity() {
        showActivity(WindowActivity::class.java)
    }

    fun onClickShowFragment() {
        showActivity(FragmentShowActivity::class.java)
    }

    fun onClickShowDisplayActivity() {
        showActivity(DisplayActivity::class.java)
    }

    fun onClickShowBatteryActivity() {
        showActivity(BatteryActivity::class.java)
    }

    fun onClickShowLocationActivity() {
        showActivity(LocationActivity::class.java)
    }

    fun onClickShowNotificationActivity() {
        showActivity(NotificationActivity::class.java)
    }

    fun onClickShowWifiActivity() {
        showActivity(WifiActivity::class.java)
    }

    fun onClickShowAlarmActivity() {
//        showActivity(AlarmActivity::class.java)
//        showActivity(LocationPermissionSampleActivity::class.java)
    }
}