package kr.open.rhpark.app.fragment

import android.Manifest
import android.view.View
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.viewmodels.BaseViewModelEventFlow

class FirstFragmentVm : BaseViewModelEventFlow<FirstFragmentVmEvent>() {
    init {
        viewModelScope.launch {

        }
    }

    fun onClickPermission(v: View) {
        Logx.d()
        sendSharedFlowEvent(
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


    fun onClickSnackBar(v: View) {
        sendSharedFlowEvent(FirstFragmentVmEvent.OnShowSnackBar("Hello SnackBar"))
    }

    fun onClickToast(v: View) {
        sendSharedFlowEvent(FirstFragmentVmEvent.OnShowToast("Hello Toast"))
    }
}