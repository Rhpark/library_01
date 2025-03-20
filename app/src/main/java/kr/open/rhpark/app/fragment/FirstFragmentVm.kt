package kr.open.rhpark.app.fragment

import android.Manifest
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.ui.viewmodels.BaseViewModelEvent

class FirstFragmentVm : BaseViewModelEvent<FirstFragmentVmEvent>() {
    init {
        viewModelScope.launch {

        }
    }

    fun onClickPermission() {
        Logx.d()
        sendEventVm(
            FirstFragmentVmEvent.OnPermissionCheck(
                listOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_PHONE_NUMBERS,
                    Manifest.permission.SYSTEM_ALERT_WINDOW
                )
            )
        )
    }


    fun onClickSnackBar() {
        sendEventVm(FirstFragmentVmEvent.OnShowSnackBar("Hello SnackBar"))
    }

    fun onClickToast() {
        sendEventVm(FirstFragmentVmEvent.OnShowToast("Hello Toast"))
    }
}