package kr.open.rhpark.app

import android.Manifest
import android.view.View
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
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


    fun onClickToastSnackBar(v: View) {
        sendEvent(MainActivityVmEvent.OnShowToastSnackBar("Hello Toast and SnackBar"))
    }

    fun onClickShowRecyclerviewActivity(v: View) {
        sendEvent(MainActivityVmEvent.OnShowRecyclerviewActivity("Show RecyclerView Activity"))
    }

    fun onClickShowVibratorActivity(v:View) {
        sendEvent(MainActivityVmEvent.OnShowVibratorActivity("Show Vibrator Activity"))
    }

    fun onClickShowUiUtilsActivity(v:View) {
        sendEvent(MainActivityVmEvent.OnShowUiUtilsActivity("Show UiUtils Activity"))
    }

    fun onClickShowFragment(v:View) {
        sendEvent(MainActivityVmEvent.OnShowFragmentActivity("Show Fragment Activity"))
    }

    fun onClickShowDisplayActivity(v:View) {
        sendEvent(MainActivityVmEvent.OnDisplayActivity("Show UiUtils Activity"))
    }
}