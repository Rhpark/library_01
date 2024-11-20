package kr.open.rhpark.app

import android.Manifest
import android.view.View
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.open.rhpark.app.activity.battery.BatteryActivity
import kr.open.rhpark.app.activity.display.DisplayActivity
import kr.open.rhpark.app.activity.network.NetworkActivity
import kr.open.rhpark.app.activity.recyclerview.RecyclerViewActivity
import kr.open.rhpark.app.activity.second.FragmentShowActivity
import kr.open.rhpark.app.activity.telephony.TelephonyActivity
import kr.open.rhpark.app.activity.toast_snackbar.ToastSnackBarActivity
import kr.open.rhpark.app.activity.usim.UsimActivity
import kr.open.rhpark.app.activity.vibrator.VibratorActivity
import kr.open.rhpark.app.activity.window.WindowActivity
import kr.open.rhpark.library.viewmodels.BaseViewModelEventFlow

class MainActivityVm : BaseViewModelEventFlow<MainActivityVmEvent>() {
    init {
        viewModelScope.launch {

        }
    }

    fun onClickPermission(v: View) {
        sendEvent(
            MainActivityVmEvent.OnPermissionCheck(
                listOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_PHONE_NUMBERS
                )
            )
        )
    }


    private fun showActivity(activity: Class<*>) {
        sendEvent(MainActivityVmEvent.OnShowActivity(activity))
    }

    fun onClickNetworkActivity(v:View) {
        showActivity(NetworkActivity::class.java)
    }

    fun onClickTelephonyActivity(v:View) {
        showActivity(TelephonyActivity::class.java)
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

    fun onClickShowUsimActivity(v:View) {
        showActivity(UsimActivity::class.java)
    }


}