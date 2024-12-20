package kr.open.rhpark.app

import android.Manifest
import android.view.View
import kr.open.rhpark.app.activity.alarm.AlarmActivity
import kr.open.rhpark.app.activity.battery.BatteryActivity
import kr.open.rhpark.app.activity.display.DisplayActivity
import kr.open.rhpark.app.activity.location.LocationActivity
import kr.open.rhpark.app.activity.network.NetworkActivity
import kr.open.rhpark.app.activity.notification.NotificationActivity
import kr.open.rhpark.app.activity.recyclerview.RecyclerViewActivity
import kr.open.rhpark.app.activity.second.FragmentShowActivity
import kr.open.rhpark.app.activity.toast_snackbar.ToastSnackBarActivity
import kr.open.rhpark.app.activity.vibrator.VibratorActivity
import kr.open.rhpark.app.activity.window.WindowActivity
import kr.open.rhpark.library.viewmodels.BaseViewModelEventFlow

class MainActivityVm : BaseViewModelEventFlow<MainActivityVmEvent>() {

    fun onClickPermission(v: View) {
        sendSharedFlowEvent(
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
        sendSharedFlowEvent(MainActivityVmEvent.OnShowActivity(activity))
    // or sendStateFlowEvent(MainActivityVmEvent.OnShowActivity(activity))
    }

    fun onClickNetworkActivity(v:View) {
        showActivity(NetworkActivity::class.java)
    }

    fun onClickToastSnackBar(v: View) {
        showActivity(ToastSnackBarActivity::class.java)
    }

    fun onClickShowRecyclerviewActivity(v: View) {
        showActivity(RecyclerViewActivity::class.java)
    }

    fun onClickShowVibratorActivity(v:View) {
        showActivity(VibratorActivity::class.java)
    }

    fun onClickShowWindowActivity(v:View) {
        showActivity(WindowActivity::class.java)
    }

    fun onClickShowFragment(v:View) {
        showActivity(FragmentShowActivity::class.java)
    }

    fun onClickShowDisplayActivity(v:View) {
        showActivity(DisplayActivity::class.java)
    }

    fun onClickShowBatteryActivity(v:View) {
        showActivity(BatteryActivity::class.java)
    }

    fun onClickShowLocationActivity(v:View) {
        showActivity(LocationActivity::class.java)
    }

    fun onClickShowNotificationActivity(v:View) {
        showActivity(NotificationActivity::class.java)
    }

    fun onClickShowAlarmActivity(v:View) {
        showActivity(AlarmActivity::class.java)
    }
}